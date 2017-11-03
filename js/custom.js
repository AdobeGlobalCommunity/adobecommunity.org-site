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
                    
                    
                    $ctx.find('.github-contact-form').submit(function () {
                        var $form = $(this);
                        var data = {
                            date: new Date()
                        };
                        $form.find('input, select, textarea').each(function(idx, el){
                            var $fld = $(el);
                            data[$fld.attr('name')] = $fld.val();
                        })
                        $form.children('fieldset').attr('disabled', 'disabled');
                        
                        var issues = AGC.gh.getIssues();

                        issues.createIssue({
                            "title": "Form Submission '" + $form.data('analytics-id') + "' on " + new Date().toLocaleDateString(),
                            "body": jsyaml.safeDump(data),
                            "labels": [
                                "form"
                            ]
                        }, function (err, res) {
                            $form.children('fieldset').removeAttr('disabled');
                            if (err) {
                                AGC.ui.alert("danger", "Unable to submit due to unexpected exception, please <a href='/contact.html'>Contact Us</a>");
                                console.log(err);
                            } else {
                                AGC.ui.alert("success", "Submitted successfully.");
                            }
                        });
                        return false;
                    });
                }
            },
            groupSearch: {
                distance: function(lat1, lon1, lat2, lon2) {
                    var radlat1 = Math.PI * lat1/180
                    var radlat2 = Math.PI * lat2/180
                    var theta = lon1-lon2
                    var radtheta = Math.PI * theta/180
                    var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
                    dist = Math.acos(dist)
                    dist = dist * 180/Math.PI
                    dist = dist * 60 * 1.1515
                    dist = dist * 0.8684
                    return dist
                },
                init: function ($ctx) {
                  $ctx.find('.group--location-search').each(function (idx, el) {
                      var $form = $(el);
                      var $ctr = $($ctx.find('.group--container'));
                      $form.submit(function(){
                          var address = $form.find('input[name=address]').val();
                          $.getJSON('https://maps.googleapis.com/maps/api/geocode/json',{
                              address: address,
                              key: 'AIzaSyDCAVkmSzjVx5ByNRpNf2hNMk17hg-DLow'
                          }, function(data){
                              if (data.results.length > 0) {
                                  var geo = data.results[0].geometry.location;
                                  $form.find('input[name=address]').val(data.results[0].formatted_address);
                                  $ctr.find('.group--item').each(function (idx, itm) {
                                      var $item = $(itm);
                                      var distance = AGC.fn.groupSearch.distance(geo.lat, geo.lng, $item.data('lat'), $item.data('lng'));
                                      $item.find('.group--distance').html(Math.round(distance, -2));
                                      $item.find('.group--distance-container').removeClass('d-none');
                                      $item.attr('data-distance', distance);
                                  });
                                  function sorter(a, b) {
                                      return a.getAttribute('data-distance') - b.getAttribute('data-distance');
                                  };
                                  var sortedDivs = $ctx.find(".group--item").toArray().sort(sorter);
                                  $ctx.find(".group--container").empty();
                                  $.each(sortedDivs, function (index, value) {
                                    $('.group--container').append(value);   //adding them to the body
                                  });
                              } else {
                                  AGC.ui.alert('warning','Unable to calculate distances to '+address);
                              }
                          });
                         return false; 
                      }); 
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
                        var filterParam = $cnt.data('filter-param');
                        if(filter && filterParam){
                            var url = new URL(location);
                            var param = url.searchParams.get(filterParam);
                            if(param){
                                filter.push({
                                    "key": filterParam,
                                    "value": param
                                })
                            }
                        }
                        $.getJSON(src, function (data) {
                            if (reverse) {
                                data.reverse();
                            }
                            if (filter) {
                                data = data.filter(function (elem) {
                                    var matches = true;
                                    filter.forEach(function(chk){
                                        if (matches) {
                                            var method = chk.method || 'equals';
                                            if (method === 'equals') {
                                                if (elem[chk.key] != chk.value) {
                                                    matches = false;
                                                } 
                                            } else if (method === 'future') {
                                                var date = new Date(elem[chk.key]);
                                                if(date <= new Date()){
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
            maps: {
                initMaps: function(){
                    $('.google-map').each(function(idx, el){
                        var pins = $(el).data('pins');
                        var template = $(el).data('template');
                        $.getJSON(pins, function(groups){
                            var latTotal = 0;
                            var lngTotal = 0;
                            var markerCount = 0;
                            groups.forEach(function(group){
                                latTotal += group.lat;
                                lngTotal += group.lng;
                                markerCount++;
                            });
                            var center = {
                                lat: latTotal / markerCount,
                                lng: lngTotal / markerCount
                            }
                            var map = new google.maps.Map(el, {
                                scrollwheel: false,
                                zoom: 2,
                                center: center
                            });
                            groups.forEach(function(group){
                                AGC.tplcb(group, template, function(content){
                                    var marker = new google.maps.Marker({
                                        position: {
                                            lat: group.lat,
                                            lng: group.lng
                                        },
                                        map: map
                                    });
                                    var infowindow = new google.maps.InfoWindow({
                                        content: content,
                                        maxWidth: 300
                                    });
                                    google.maps.event.addListener(marker, 'click', function() {
                                        infowindow.open(map,marker);
                                    });
                                });
                            });

                        });
                    });
                }  
            },
            meetupevents: {
                init: function ($ctx) {
                    $ctx.find('.meetup-event--container').each(function(){
                        var $ctr = $(this);
                        var meetupId = $ctr.data('meetup-id');
                        var accessToken = localStorage.getItem('meetup_access_token',accessToken);
                        if (accessToken != null) {
                            $.ajax({
                                url: 'https://api.meetup.com'+meetupId+'events?access_token='+accessToken,
                                method: 'GET',
                                dataType: 'json',
                                success: function(events){
                                    
                                    var count = 0;
                                    for (var idx in events) {
                                        var event = events[idx];
                                        var date = new Date(event.local_date);
                                        var locale = "en-us";
                                        event.date = {
                                            "dayofmonth": event.getDate(),
                                            "dayofweek": date.toLocaleString(locale, {  weekday: 'long' }),
                                            "month": date.toLocaleString(locale, { month: "long" }),
                                            "year": date.getYear()
                                        };
                                        AGC.tpl(event, 'meetup-event', $ctr);
                                        count++;
                                    }
                                    if(count == 0){
                                        $ctr.append("<em>No upcoming events found...</em>");
                                    }
                                    $ctx.find('.meetup-event--oauth').hide();
                                },
                                error: function(jqXHR, textStatus, errorThrown){
                                    console.log('Retrieved invalid response from Meetup: '+textStatus);
                                    localStorage.removeItem('meetup_access_token');
                                }
                            });
                        }
                    });
                    $ctx.find('.meetup-event--button').click(function(){
                        window.open($(this).attr('href'),'_blank','height=550,width=500,titlebar=no,toolbar=no');
                        return false;
                    });
                    if($('body').data('path') == 'meetup-auth.html'){
                        const hash = '?'+window.location.hash.substring(1);
                        const accessToken = (new URL('http://www.google.com'+hash)).searchParams.get('access_token');
                        localStorage.setItem('meetup_access_token',accessToken);
                        window.opener.location.reload(false);
                        window.close();
                    }
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
        tplcb: function (data, name, cb) {
            if (AGC.tpls[name]) {
                $cnt.append(AGC.tpls[name](data));
            }
            $.ajax({
                url: '/templates/' + name + '.hbs',
                cache: true,
                success: function (hbs) {
                    AGC.tpls[name] = Handlebars.compile(hbs);
                    cb(AGC.tpls[name](data));
                }
            });
        },
        tpl: function (data, name, $cnt) {
            AGC.tplcb(data, name, function(content){
                 $cnt.append(content);
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
    window.AGC=AGC;
    $(document).ready(function() {
        AGC.init($(document));
    });
})(jQuery);