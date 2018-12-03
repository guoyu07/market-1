if (typeof acqMerchantInfo == 'undefined') {
    acqMerchantInfo = {}
}

acqMerchantInfo.list = {
    acqChannelData: null,
    acqRateTypeData: null,

    init: function () {
        $("select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            acqMerchantInfo.list.qryPageData();
        });
        $("#frmQry").on("submit", function () {
            acqMerchantInfo.list.qryPageData();
            return false;
        });

        var _merchantNo = $("#merchantNo");
        var _terminalNo = $("#terminalNo");
        _merchantNo.select2({
            width: "100%",
            language: "zh-CN",
            placeholder: "输入商户账号或者商户名称",
            cache: "true",
            allowClear: true,
            ajax: {
                url: ctx + "/merchant/map",
                dataType: 'json',
                type: "POST",
                delay: 250,
                data: function (params) {
                    return {
                        likeKey: params.term,
                        page: params.page,
                        size: 20
                    };
                },
                processResults: function (data, page) {
                    if (data.status == "OK") {
                        return {
                            results: $.map(data.message, function (value, key) {
                                return {id: key, text: value};
                            })
                        };
                    } else {
                        alert(data.message);
                    }
                }

            },
            escapeMarkup: function (markup) {
                return markup;
            },
            minimumInputLength: varSearchKeyMinLength,
            maximumInputLength: 20
        }).on("change", function () {
            _terminalNo.empty();
            _terminalNo.append($("<option>").attr({"value": ""}).html("-请选择-"));
            _terminalNo.val("").trigger("change");
            if ($(this).val() != '') {
                var _params = {"merchantNo": $(this).val()};
                ucf.ajax.commonFn(ctx + "/terminal/map", _params, function (result) {
                    if (result.status == 'OK') {
                        $.each(result.message, function (k, v) {
                            $("<option>").attr({"value": k}).html(k + "(" + v + ")").appendTo(_terminalNo);
                        });
                    }
                });
            }
        });

        if (acqMerchantInfo.list.acqChannelData == null) {
            acqMerchantInfo.list.acqChannelData = acqMerchantInfo.common.getAcqChannel();
        }

        if (acqMerchantInfo.list.acqRateTypeData == null) {
            acqMerchantInfo.list.acqRateTypeData = acqMerchantInfo.common.getAcqRateType();
        }

        acqMerchantInfo.list.qryPageData();
    },

    qryPageData: function () {
        acqMerchantInfo.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: acqMerchantInfo.list.PageCallback,
                prev_text: '上一页',
                next_text: '下一页',
                items_per_page: $("input[name='size']").val(),
                num_display_entries: 5,
                current_page: $("input[name='page']").val(),
                load_first_page: false
            });
        }
    },

    getRecords: function () {
        ucf.ajax.commonFn(ctx + "/acqMerchantInfo/list", $("#frmQry").serialize(), function (result) {
            if (result.status == 'OK') {
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var _noRecord = $("<tr>").append($("<td>").attr({
                    "colspan": $("#tblData tr th").length,
                    "style": "text-align: center"
                }).html("没有搜索到数据"));
                if (typeof result.message.content == "undefined" || result.message.content.length == 0) {
                    $("#tblData tbody").append(_noRecord);
                } else {
                    $.each(result.message.content, function (index, obj) {
                        var _tr = $("<tr>");
                        $("<td>").html($("<a>").attr({
                            "href": ctx + "/acqMerchantInfo/" + obj.merchantNo + "/" + obj.terminalNo + ucf.common.getUrlRandom(),
                            "data-toggle": "modal",
                            "data-target": "#myModal"
                        }).html(obj.merchantNo + " | " + obj.merchantName)).appendTo(_tr);
                        $("<td>").html(obj.terminalLabel).appendTo(_tr);
                        $("<td>").html(acqMerchantInfo.util.channelDisplay(obj.channelNo)).appendTo(_tr);
                        $("<td>").html(obj.channelMerchant).appendTo(_tr);
                        $("<td>").html(obj.channelTerminal).appendTo(_tr);
                        $("<td>").html(obj.routeFlagLabel).appendTo(_tr);
                        $("<td>").html(obj.settleTypeLabel).appendTo(_tr);
                        $("<td>").html(obj.appTime).appendTo(_tr);
                        $("<td>").html(obj.appOpno).appendTo(_tr);
                        $("#tblData tbody").append(_tr);
                    });
                }
            } else {
                $.pnotify({title: result.message, type: 'error'});
            }
        });
    },

    PageCallback: function (pageIndex, jq) {
        $("input[name='page']").val(pageIndex);
        acqMerchantInfo.list.getRecords();
    },

    setQueryCondition: function (param) {
        //alert(param.merchantNoVal + "|" + param.merchantNoText);
        //var _merchantNo = $("#merchantNo");
        var _merchantNo = $("select[name='merchantNo']").eq(1);
        _merchantNo.append($("<option>").attr({"value": param.merchantNoVal}).html(param.merchantNoText));
        _merchantNo.val(param.merchantNoVal).trigger("change");
    }
};

acqMerchantInfo.info = {
    init: function () {
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity, width: '100%'});
        $("#merchantNo").select2({width: '100%'});

        var _merchantNo = $("#merchantNo");
        var _terminalNo = $("#terminalNo");
        var _channelNo = $("#channelNo");
        var _channelMerchant = $("#channelMerchant");
        var _channelTerminal = $("#channelTerminal");
        _merchantNo.on("change", function () {
            _terminalNo.empty();
            _terminalNo.append($("<option>").attr({"value": ""}).html("-请选择-"));
            _terminalNo.val("").trigger("change");
            var _merchantNo = $(this).val();
            ucf.ajax.commonFn(ctx + "/acqMerchantInfo/queryAcqBusiMerchantTerminal", {
                "merchantNo": _merchantNo,
                "queryType": "T"
            }, function (data) {
                if (data.status == 'OK') {
                    $.each(data.message, function (k, v) {
                        $("<option>").attr({"value": k}).html(k + '(' + v + ')').appendTo(_terminalNo);
                    });
                }
            });
        });

        _channelMerchant.select2({
            width: "100%",
            allowClear: true,
            language: "zh-CN",
            placeholder: "请先选择通道号，再输入商户账号或者商户名称",
            cache: "true",
            ajax: {
                url: ctx + "/acqChannelMerchant/map",
                dataType: 'json',
                type: "POST",
                delay: 250,
                data: function (params) {
                    return {
                        queryType: "M",
                        channelNo: _channelNo.val(),
                        merchantLikeKey: params.term,
                        page: params.page,
                        size: 20
                    };
                },
                processResults: function (data, page) {
                    if (data.status == "OK") {
                        return {
                            results: $.map(data.message, function (value, key) {
                                return {id: key, text: value.merchantName};
                            })
                        };
                    } else {
                        alert(data.message);
                    }
                }

            },
            escapeMarkup: function (markup) {
                return markup;
            },
            minimumInputLength: varSearchKeyMinLength,
            maximumInputLength: 20
        }).on("change", function () {
            _channelTerminal.empty();
            _channelTerminal.append($("<option>").attr({"value": ""}).html("-请选择-"));
            _channelTerminal.val("").trigger("change");
            var _val = $(this).val();
            if (_val != '') {
                ucf.ajax.commonFn(ctx + "/acqChannelMerchant/map", {
                    channelNo: _channelNo.val(),
                    "merchantLikeKey": _val,
                    "queryType": "T"
                }, function (data) {
                    if (data.status == 'OK') {
                        $.each(data.message, function (key, value) {
                            $("<option>").attr({"value": value.terminalNo}).html(value.terminalNo).appendTo(_channelTerminal);
                        });
                    }
                });
            }
        });


        if (_channelMerchant.attr("defVal") != '') {
            $("<option>").attr({"value": _channelMerchant.attr("defVal")}).html(_channelMerchant.attr("defText")).appendTo(_channelMerchant);
            _channelMerchant.val(_channelMerchant.attr("defVal")).trigger("change");
        }

        if (_channelTerminal.attr("defVal") != '') {
            _channelTerminal.val(_channelTerminal.attr("defVal")).trigger("change");
        }

        $("#frmEdit").validate({
            rules: {
                merchantNo: {
                    required: true
                },
                terminalNo: {
                    required: true
                },
                channelNo: {
                    required: function () {
                        return $("#routeFlag").val() == 0;
                    }
                },
                channelMerchant: {
                    required: function () {
                        return $("#routeFlag").val() == 0;
                    }
                },
                channelTerminal: {
                    required: function () {
                        return $("#routeFlag").val() == 0 || $("#channelMerchant").val() != '';
                    }
                },
                routeFlag: {
                    required: true
                }
            },
            messages: {},
            ignore: 'input[type=hidden]',
            errorPlacement: function (error, element) {
                if (element.is(":checkbox")) {
                    error.appendTo(element.parent());
                } else if (element.is(":text")) {
                    element.after(error);
                } else {
                    error.appendTo(element.next());
                }
            },
            submitHandler: function (form) {
                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                    if (result.status == 'OK') {
                        //acqMerchantInfo.list.setQueryCondition(acqMerchantInfo.info.getQueryCondition());
                        acqMerchantInfo.list.getRecords();
                        $('[data-dismiss]').click();
                        $.pnotify({title: result.message, type: result.status});
                    } else {
                        $("#saveBtn").attr("data-content", result.message);
                        $("#saveBtn").popover('toggle');
                        setTimeout(function () {
                            $("#saveBtn").popover('toggle');
                        }, 2000);
                    }
                });
            }
        });

        $("#saveBtn").on("click", function () {
            $("#frmEdit").submit();
        });

        $("#delBtn").on("click", function () {
            var _param = {
                "_method": "delete",
                "merchantNo": $("input[name='merchantNo']").val(),
                "terminalNo": $("input[name='terminalNo']").val()
            };
            confirm_dialog("操作确认", "确定删除吗？删除后数据不可恢复！", function (notice, val) {
                if (notice) {
                    ucf.ajax.commonFn($('#frmEdit').attr('action'), _param, function (result) {
                        if (result.status == 'OK') {
                            //acqMerchantInfo.list.setQueryCondition(acqMerchantInfo.info.getQueryCondition());
                            acqMerchantInfo.list.getRecords();
                            $('[data-dismiss]').click();
                            $.pnotify({title: result.message, type: result.status});
                        } else {
                            $("#delBtn").attr("data-content", result.message);
                            $("#delBtn").popover('toggle');
                            setTimeout(function () {
                                $("#delBtn").popover('toggle');
                            }, 2000);
                        }
                    });
                }
            });
        });
    },


    getQueryCondition: function () {
        var _method = $("#_method");
        var _param = {};
        if ("post" == _method.val()) {
            _param = {
                "merchantNoVal": $("select[name='merchantNo']").eq(0).val(),
                "merchantNoText": $("select[name='merchantNo']").eq(0).find("option:selected").text()
            }
        } else if ("put" == _method.val()) {
            _param = {
                "merchantNoVal": $("input[name='merchantNo']").eq(0).val(),
                "merchantNoText": $("input[name='merchantName']").eq(0).val()
            }
        }
        return _param;
    }
};


acqMerchantInfo.common = {
    getAcqChannel: function () {
        var map = new Map();
        ucf.ajax.commonFn(ctx + "/acqChannel/getChannel", null, function (acqChannelData) {
            if (acqChannelData.status == 'OK') {
                $.each(acqChannelData.message, function (index, obj) {
                    map.put(obj.channelNo, obj.channelName);
                });
            }
        });
        return map;
    },

    getAcqRateType: function () {
        var map = new Map();
        ucf.ajax.commonFn(ctx + "/acqRateTypeInfo/getRateType", null, function (acqRateTypeInfoData) {
            if (acqRateTypeInfoData.status == 'OK') {
                $.each(acqRateTypeInfoData.message, function (index, obj) {
                    map.put(obj.rateType, obj.rateName);
                });
            }
        });
        return map;
    }
};


acqMerchantInfo.util = {
    channelDisplay: function (channelNo) {
        if (typeof channelNo == 'undefined' || channelNo == '' || channelNo == null) {
            return "";
        } else {
            return channelNo + " | " + acqMerchantInfo.list.acqChannelData.get(channelNo);
        }
    },

    rateTypeDisplay: function (rateType) {
        if (typeof rateType == 'undefined' || rateType == '' || rateType == null) {
            return "";
        } else {
            return acqMerchantInfo.list.acqRateTypeData.get(rateType);
        }
    }
};
