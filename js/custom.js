(function ($) {
    var AGC = {
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
        gh: {
            accessToken: 'ZTZiZWFiNmRlZTI4N2U0YzBkMGNiNjgzMGVlY2FjY2QzZGY1NmU1NQ==',
            getIssues: function(){
                var gh = new GitHub({
                    token: atob(App.gh.accessToken)
                });
                return gh.getIssues("AdobeGlobalCommunity/adobeglobalcommunity.org-site");
            },
            getRepo: function(){
                var gh = new GitHub();
                return gh.getRepo("AdobeGlobalCommunity/adobeglobalcommunity.org-site");
            }
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
    
    $('.github-form').submit(function(){
        $form = $(this);
        $form.children('fieldset').attr('disabled','disabled');
        var properties = {
            date: new Date()
        };
        $form.find('fieldset > .form-group').each(function(idx, fieldGroup){
            if($(fieldGroup).hasClass('repeating-parent')){
                var key = $(fieldGroup).attr('data-name');
                var values = [];
                $fields = $($(fieldGroup).find('.repeating-container input,.repeating-container select'));
                $fields.each(function(idx, field){
                    values.push($(field).val());
                });
                properties[key] = values;
            } else {
                if($(fieldGroup).find('textarea[name=content]').length > 0){
                    // skip
                } else {
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
        var page = "---\n"+jsyaml.safeDump(properties)+"\n---\n\n"+$form.find('textarea[name=content]').val();
        console.log("Updated content "+page);
        var path = null;
        if(window.location.hash === ""){
            path = $('input[name=path]').val()+properties.title.toLowerCase().replace(/\W/g,'-')+'.html';
        } else {
            path = window.location.hash.substr(1);
        }

        var issues = AGC.gh.getIssues();

        issues.createIssue({
            "title": "Update "+path+" by "+properties.author+" on "+new Date().toLocaleDateString(),
            "body": page,
            "labels": [
                "content"
            ]
        }, function(err, res){
            $form.children('fieldset').removeAttr('disabled');
            if(err){
                App.ui.alert("danger","Unable to submit due to unexpected exception, please <a href='/contact.html'>Contact Us</a>");
                console.log(err);
            } else {
                App.ui.alert("success","Submitted successfully. Changes should be reflected within 24-48 hours.");
            }
        });
        return false;
    });
    
    $('.ajax--load').each(function (idx, el) {
        var $cnt = $(el);
        var $loader = $cnt.find('.ajax--loader');
        var src = $cnt.data('src');
        var tpl = $cnt.data('tpl');
        var reverse = $cnt.data('reverse') || false;
        var limit = $cnt.data('limit') || 2147483647;
        $.getJSON(src, function (data) {
            if (reverse) {
                data.reverse();
            }
            data.forEach(function (el, idx) {
                if (idx < limit) {
                    AGC.tpl(el, tpl, $cnt);
                }
            });
            $loader.hide();
        });
    });
})(jQuery);