(function ($) {
    var AGC = {
        init: function ($ctx) {
            var key = null;
            for (key in AGC.fn) {
                if (AGC.fn[key].hasOwnProperty('init')) {
                    AGC.fn[key].init($ctx);
                }
            }
        },
        fn: {
            form: {
                init: function ($ctx) {
                    $ctx.find('.github-form').submit(function () {
                        var $form = $(this);
                        $form.children('fieldset').attr('disabled', 'disabled');
                        var properties = {
                            date: new Date()
                        };
                        $form.find('fieldset > .form-group').each(function (idx, fieldGroup) {
                            if ($(fieldGroup).hasClass('repeating-parent')) {
                                var key = $(fieldGroup).attr('data-name');
                                var values = [];
                                var $fields = $($(fieldGroup).find('.repeating-container input,.repeating-container select'));
                                $fields.each(function(idx, field){
                                    values.push($(field).val());
                                });
                                properties[key] = values;
                            } else {
                                if (! ($(fieldGroup).find('textarea[name=content]').length > 0)) {
                                    $fields = $($(fieldGroup).find('input,select'));
                                    $fields.each(function(idx, field){
                                        properties[$(field).attr('name')]=$(field).val();
                                    });
                                }
                            }
                        });

                        if(properties.created == ''){
                            properties.created = new Date();
                        } else {
                            properties.created = new Date(Date.parse(properties.created));
                        }
                        var page = "---\n" + jsyaml.safeDump(properties) + "\n---\n\n" + $form.find('textarea[name=content]').val();
                        console.log("Updated content "+page);
                        var path = null;
                        if(window.location.hash === ""){
                            path = $('input[name=path]').val() + properties.title.toLowerCase().replace(/\W/g,'-') + '.html';
                        } else {
                            path = window.location.hash.substr(1);
                        }

                        var issues = AGC.gh.getIssues();

                        issues.createIssue({
                            "title": "Add " + path + " by " + properties.author + " on " + new Date().toLocaleDateString(),
                            "body": page,
                            "labels": [
                                "content"
                            ]
                        }, function (err, res) {
                            $form.children('fieldset').removeAttr('disabled');
                            if (err) {
                                AGC.ui.alert("danger", "Unable to submit due to unexpected exception, please <a href='/contact.html'>Contact Us</a>");
                                console.log(err);
                            } else {
                                AGC.ui.alert("success", "Submitted successfully. Changes should be reflected within 24-48 hours.");
                            }
                        });
                        return false;
                    });
                }
            },
            loader: {
                init : function ($ctx) {
                    $ctx.find('.ajax--load').each(function (idx, el) {
                        var $cnt = $(el);
                        var $loader = $cnt.find('.ajax--loader');
                        var src = $cnt.data('src');
                        var tpl = $cnt.data('tpl');
                        var filter = $cnt.data('filter');
                        var reverse = $cnt.data('reverse') || false;
                        var limit = $cnt.data('limit') || 2147483647;
                        $.getJSON(src, function (data) {
                            if (reverse) {
                                data.reverse();
                            }
                            if (filter) {
                                data = data.filter(function (elem) {
                                    var matches = true;
                                    filter.forEach(function(check){
                                        if (matches) {
                                            var method = check.method || 'equals';
                                            var key = check.key;
                                            var value = check.value;
                                            if (method === 'equals') {
                                                if (elem[key] != check.value) {
                                                    matches = false;
                                                } 
                                            } else if (method === 'equals') {
                                                if (elem[key] != check.value) {
                                                    matches = false;
                                                } 
                                            }
                                        }
                                    });
                                    return matches;
                                });
                            }
                            data.forEach(function (el, idx) {
                                el.index = idx;
                                el.first = (idx === 0);
                                if (idx < limit) {
                                    AGC.tpl(el, tpl, $cnt);
                                }
                            });
                            $loader.hide();
                        });
                    });
                }
            },
            repeating: {
                repeatingAdd: function ($btn) {
                    var html = $btn.closest('.repeating-parent').find('.repeating-template').first().html();
                    var $container = $btn.closest('.row').siblings('.repeating-container');
                    var $div = $('<div class="repeating-item"></div>');
                    $div.append(html);
                    var current = parseInt($container.attr('data-current'), 10);
                    $container.attr('data-current', current + 1);
                    AGC.init($div);
                    $container.append($div);
                    return $div;
                },
                repeatingRemove: function () {
                    $(this).closest('.repeating-item').remove();
                    return false;
                },
                init: function ($ctx) {
                    $ctx.find('.repeating-add').click(function () {
                        AGC.fn.repeating.repeatingAdd($(this));
                        return false
                    });
                    $ctx.find('.repeating-remove').click(AGC.fn.repeating.repeatingRemove);
                }
            },
            tags: {
                init: function ($ctx) {
                    $ctx.find('#tag-options').each(function (idx, el) {
                        var $ops = $(el);
                        $.getJSON("/tags.json", function(tags){
                            tags.sort(function(a, b){
                                return a.name.toLowerCase().localeCompare(b.name.toLowerCase());
                            });
                            tags.forEach(function(tag){
                                $ops.append("<option>" + tag.name + "<option>");
                            });
                        })
                    });
                }
            }
        },
        gh: {
            accessToken: 'ZTZiZWFiNmRlZTI4N2U0YzBkMGNiNjgzMGVlY2FjY2QzZGY1NmU1NQ==',
            getIssues: function () {
                var gh = new GitHub({
                    token: atob(AGC.gh.accessToken)
                });
                return gh.getIssues("AdobeGlobalCommunity/adobeglobalcommunity.org-site");
            },
            getRepo: function () {
                var gh = new GitHub();
                return gh.getRepo("AdobeGlobalCommunity/adobeglobalcommunity.org-site");
            }
        },
        tpls: {},
        tpl: function (data, name, $cnt) {
            if (AGC.tpls[name]) {
                $cnt.append(AGC.tpls[name](data));
            }
            $.ajax({
                url: '/templates/' + name + '.hbs',
                cache: true,
                success: function (hbs) {
                    AGC.tpls[name] = Handlebars.compile(hbs);
                    $cnt.append(AGC.tpls[name](data));
                }
            });
        },
        ui: {
            alert: function(level, message){
                $alert = $('<div class="alert alert-'+level+'">'+message+'</div>');
                $('.main').prepend($alert);
                setTimeout(function(){
                    $alert.remove();
                }, 10000);
                window.scrollTo(0, 0);
            }
        }
    };
    
    $(document).ready(function() {
        AGC.init($(document));
    });
})(jQuery);