(function ($) {
    "use strict";
    var AGC = {
        init: function ($ctx) {
            var key = null;
            $.each(AGC.fn, function (ns, fn) {
                if (fn.hasOwnProperty('init')) {
                    fn.init($ctx);
                }
            });
        },
        fn: {
        	cug: {
        		hide: function($cug){
        			$cug.css('display','none');
        		},
        		init: function($ctx){
        			
        			if(!AGC.profile.initialized){
        				AGC.fn.profile.init($ctx);
        			}
        			$ctx.find('.agc__cug').each(function(idx, el){
        				var $cug = $(el);
        				if($cug.data('auth')){
        					if(AGC.profile.loggedIn){
        						AGC.fn.cug.show($cug);
        					} else {
        						AGC.fn.cug.hide($cug);
        					}
        				} else {
        					if(!AGC.profile.loggedIn){
        						AGC.fn.cug.show($cug);
        					} else {
        						AGC.fn.cug.hide($cug);
        					}
        				}
        			});
        		},
        		show: function($cug){
        			$cug.css('display','block');
        			if($cug.data('template')){
        				AGC.tplcb(AGC.profile, $cug.data('template'), function (content) {
        					 $cug.html(content);
        				});
        			}
        		}
        	},
            groupSearch: {
                distance: function (lat1, lon1, lat2, lon2) {
                    var radlat1 = Math.PI * lat1 / 180;
                    var radlat2 = Math.PI * lat2 / 180;
                    var theta = lon1 - lon2;
                    var radtheta = Math.PI * theta / 180;
                    var dist = Math.sin(radlat1) * Math.sin(radlat2) + Math.cos(radlat1) * Math.cos(radlat2) * Math.cos(radtheta);
                    dist = Math.acos(dist);
                    dist = dist * 180 / Math.PI;
                    dist = dist * 60 * 1.1515;
                    dist = dist * 0.8684;
                    return dist;
                },
                init: function ($ctx) {
                    $ctx.find('.group--location-search').each(function (idx, el) {
                        var $form = $(el);
                        var $ctr = $($ctx.find('.group--container'));
                        var $ldr = $($ctr.find('.ajax-load__loader'));
                        $form.submit(function () {
                            $($ctr.find('.group--item')).hide();
                            $ldr.show();
                            var address = $form.find('input[name=address]').val();
                            $.getJSON('https://maps.googleapis.com/maps/api/geocode/json', {
                                address: address,
                                key: 'AIzaSyDCAVkmSzjVx5ByNRpNf2hNMk17hg-DLow'
                            }, function (data) {
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

                                    var sorter = function (a, b) {
                                        return a.getAttribute('data-distance') - b.getAttribute('data-distance');
                                    };
                                    var sortedDivs = $ctx.find(".group--item").toArray().sort(sorter);
                                    $ctx.find(".group--container").empty();
                                    $.each(sortedDivs, function (index, value) {
                                        $('.group--container').append(value); //adding them to the body
                                    });
                                    $ldr.hide();
                                    $($ctr.find('.group--item')).show();
                                } else {
                                    AGC.ui.alert('warning', 'Unable to calculate distances to ' + address);
                                }
                            });
                            return false;
                        });
                    });
                }
            },
            loader: {
                init: function ($ctx) {
                    $ctx.find('.ajax-load').each(function (idx, el) {
                        var $cnt = $(el),
                            $loader = $cnt.find('.ajax-load__loader'),
                            $ctr = $cnt.find('.ajax-load__container'),
                            src = $cnt.data('src'),
                            tpl = $cnt.data('tpl'),
                            filter = $cnt.data('filter'),
                            reverse = $cnt.data('reverse') || false,
                            limit = $cnt.data('limit') || 2147483647,
                            filterParam = $cnt.data('filter-param'),
                            pageSize = $cnt.data('page-size'),
                            page = 0;
                        filter = JSON.parse(filter.replace(/'/g, '"'));
                        var load = function(start, end){
                            var dfd = jQuery.Deferred();
                            if (filterParam) {
                                if (!filter) {
                                    filter = [];
                                }
                                var url = new URL(location);
                                var param = url.searchParams.get(filterParam);
                                if (param) {
                                    filter.push({
                                        "key": filterParam,
                                        "value": param
                                    })
                                }
                            }
                            $.getJSON(src, function (data) {
                            	if(!Array.isArray(data)){
                            		data = data.data;
                            	}
                                if (reverse) {
                                    data.reverse();
                                }
                                if (filter) {

                                    data = data.filter(function (elem) {
                                        var matches = true;
                                        filter.forEach(function (chk) {
                                            if (matches) {
                                                var method = chk.method || 'equals';
                                                if (method === 'equals') {
                                                    if (elem[chk.key].constructor === Array) {
                                                        if (elem[chk.key].indexOf(chk.value) === -1) {
                                                            matches = false;
                                                        }
                                                    } else {
                                                        if (elem[chk.key] != chk.value) {
                                                            matches = false;
                                                        }
                                                    }
                                                } else if (method === 'future') {
                                                    var date = new Date(elem[chk.key]);
                                                    if (date <= new Date()) {
                                                        matches = false;
                                                    }
                                                }
                                            }
                                        });
                                        return matches;
                                    });
                                }

                                data = data.slice(start, end);
                                data.forEach(function (el, idx) {
                                    el.index = idx;
                                    el.first = (idx === 0);
                                });
                                AGC.tpl(data, tpl, ($ctr.length > 0 ? $ctr : $cnt));
                                $loader.hide();
                                dfd.resolve();
                            });
                            return dfd.promise();
                        }
                        var end = limit;
                        if(pageSize){
                            end = pageSize;
                        }
                        load(page, end);
                        $cnt.find('.ajax-load__more').click(function(){
                            var $btn = $(this);
                            $btn.attr('disabled', 'disabled');
                            page = page + pageSize;
                            load(page, pageSize + page).done(function(){
                                $btn.removeAttr('disabled');
                            });
                            return false;
                        });
                    });
                }
            },
            maps: {
                initMaps: function () {
                    $('.google-map').each(function (idx, el) {
                        var pins = $(el).data('pins');
                        var template = $(el).data('template');
                        $.getJSON(pins, function (groups) {
                            var latTotal = 0;
                            var lngTotal = 0;
                            var markerCount = 0;
                            groups.data.forEach(function (group) {
                                latTotal += parseFloat(group.lat);
                                lngTotal += parseFloat(group.lng);
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
                            groups.data.forEach(function (group) {
                                AGC.tplcb(group, template, function (content) {
                                    var marker = new google.maps.Marker({
                                        position: {
                                            lat: parseFloat(group.lat),
                                            lng: parseFloat(group.lng)
                                        },
                                        map: map
                                    });
                                    var infowindow = new google.maps.InfoWindow({
                                        content: content,
                                        maxWidth: 300
                                    });
                                    google.maps.event.addListener(marker, 'click', function () {
                                        infowindow.open(map, marker);
                                    });
                                });
                            });
                        });
                    });
                }
            },
            meetupevents: {
                init: function ($ctx) {
                    $ctx.find('.meetup-event--container').each(function () {
                        var $ctr = $(this);
                        var meetupId = $ctr.data('meetup-id');
                        var accessToken = localStorage.getItem('meetup_access_token', accessToken);
                        if (accessToken != null) {
                            $.ajax({
                                url: 'https://api.meetup.com' + meetupId + 'events?access_token=' + accessToken,
                                method: 'GET',
                                dataType: 'json',
                                success: function (events) {

                                    var count = 0;
                                    for (var idx in events) {
                                        var event = events[idx];
                                        var date = new Date(event.local_date);
                                        var locale = "en-us";
                                        event.date = {
                                            "dayofmonth": date.getDate(),
                                            "dayofweek": date.toLocaleString(locale, {
                                                weekday: 'long'
                                            }),
                                            "month": date.toLocaleString(locale, {
                                                month: "long"
                                            }),
                                            "year": date.getFullYear()
                                        };
                                        count++;
                                    }
                                    AGC.tpl(events, 'meetup-event', $ctr);
                                    if (count == 0) {
                                        $ctr.append("<em>No upcoming events found...</em>");
                                    }
                                    $ctx.find('.meetup-event--oauth').hide();
                                },
                                error: function (jqXHR, textStatus, errorThrown) {
                                    console.log('Retrieved invalid response from Meetup: ' + textStatus);
                                    localStorage.removeItem('meetup_access_token');
                                }
                            });
                        }
                    });
                    $ctx.find('.meetup-event--button').click(function () {
                        window.open($(this).attr('href'), '_blank', 'height=550,width=500,titlebar=no,toolbar=no');
                        return false;
                    });
                    if ($('body').data('path') == 'meetup-auth.html') {
                        var hash = '?' + window.location.hash.substring(1);
                        var accessToken = (new URL('http://www.google.com' + hash)).searchParams.get('access_token');
                        localStorage.setItem('meetup_access_token', accessToken);
                        window.opener.location.reload(false);
                        window.close();
                    }
                }
            },
            paramval: {
                init: function ($ctx) {
                    var url = new URL(window.location);
                    $ctx.find('.param-val').each(function () {
                        $(this).html(url.searchParams.get($(this).data('param')));
                    });
                }
            },
			profile : {
				init : function($ctx) {
					if (AGC.profile.initialized === false) {
						if(sessionStorage.hasOwnProperty('profile')){
							AGC.profile = JSON.parse(sessionStorage.getItem("profile"));
						} else {
							$.getJSON(window.location.pathname.replace('.html','.model.json'), function(profile){
								AGC.profile = profile;
								sessionStorage.setItem('profile', JSON.stringify(profile));
							});
						}
						AGC.profile.initialized = true;
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
            resetLogin: {
            	init: function ($ctx) {
            		$ctx.find('a[href=/system/sling/logout]').click(AGC.fn.resetLogin.reset);
            		$ctx.find('form[data-analytics-id="Login Form"]').submit(AGC.fn.resetLogin.reset);
            	},
            	reset: function(){
            		sessionStorage.removeItem("profile");
            	}
            },
            tags: {
                init: function ($ctx) {
                    $ctx.find('#tag-options').each(function (idx, el) {
                        var $ops = $(el);
                        $.getJSON("/tags.json", function (tags) {
                            tags.sort(function (a, b) {
                                return a.name.toLowerCase().localeCompare(b.name.toLowerCase());
                            });
                            tags.forEach(function (tag) {
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
        profile: {
        	initialized: false
        },
        tpls: {},
        tplcb: function (data, name, cb) {
            if (AGC.tpls[name]) {
                if (Array.isArray(data)) {
                    data.forEach(function (item) {
                        cb(AGC.tpls[name](item));
                    });
                } else {
                    cb(AGC.tpls[name](data));
                }
            } else {
                $.ajax({
                    url: '/static/clientlibs/adobecommunity-org/templates/' + name + '.hbs',
                    cache: true,
                    success: function (hbs) {
                        AGC.tpls[name] = Handlebars.compile(hbs);
                        if (Array.isArray(data)) {
                            data.forEach(function (item) {
                                cb(AGC.tpls[name](item));
                            });
                        } else {
                            cb(AGC.tpls[name](data));
                        }
                    }
                });
            }
        },
        tpl: function (data, name, $cnt) {
            AGC.tplcb(data, name, function (content) {
                $cnt.append(content);
            });
        },
        ui: {
            alert: function (level, message) {
                var $alert = $('<div class="alert alert-' + level + '">' + message + '</div>');
                $('.main').prepend($alert);
                setTimeout(function () {
                    $alert.remove();
                }, 10000);
                window.scrollTo(0, 0);
            }
        }
    };
    window.AGC = AGC;
    $(document).ready(function () {
        AGC.init($(document));
    });
})(jQuery);
