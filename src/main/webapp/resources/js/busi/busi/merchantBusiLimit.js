if (typeof merchantBusiLimit == 'undefined') {
    merchantBusiLimit = {}
}
merchantBusiLimit.list = {
    init: function () {
        $("select").select2({minimumResultsForSearch: Infinity, width: '100%'});

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
            _terminalNo.append($("<option>").attr({"value": "00000000"}).html("-全部-"));
            if ($(this).val() != '') {
                ucf.ajax.commonFn(ctx + "/terminal/map", {"merchantNo": $(this).val()}, function (data) {
                    $.map(data.message, function (value, key) {
                        var _option = $("<option>").attr({"value": key}).html(key + " (" + value + ") ");
                        _terminalNo.append(_option);
                    });
                });
            }
        });


        $("#busiCode").select2({
            width: "100%",
            language: "zh-CN",
            placeholder: "输入业务编码或名称",
            cache: "true",
            allowClear: true,
            ajax: {
                url: ctx + "/busiInfo/map",
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
        });


        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            $("#frmQry").submit();
        });

        $("#frmQry").on("submit", function () {
            merchantBusiLimit.list.qryPageData();
            return false;
        });
        merchantBusiLimit.list.qryPageData();
    },

    qryPageData: function () {
        merchantBusiLimit.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: merchantBusiLimit.list.PageCallback,
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
        ucf.ajax.commonFn(ctx + "/merchantBusiLimit/list", $("#frmQry").serialize(), function (result) {
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
                            "href": ctx + "/merchantBusiLimit/" + obj.merchantNo + "/" + obj.busiCode + "/" + obj.terminalNo + ucf.common.getUrlRandom(),
                            "data-toggle": "modal",
                            "data-target": "#myModal"
                        }).html(obj.merchantNo + " | " + obj.merchantName)).appendTo(_tr);
                        $("<td>").html(obj.busiCode + " | " + obj.busiName).appendTo(_tr);
                        $("<td>").html(obj.terminalNameLabel).appendTo(_tr);
                        $("<td>").html(ucf.common.formatNum(obj.biQuota, 2)).appendTo(_tr);
                        $("<td>").html(ucf.common.formatNum(obj.dayQuota, 2)).appendTo(_tr);
                        $("<td>").html(ucf.common.formatNum(obj.monthQuota, 2)).appendTo(_tr);
                        $("<td>").html(ucf.common.formatNum(obj.yearQuota, 2)).appendTo(_tr);
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
        merchantBusiLimit.list.getRecords();
    }


};


merchantBusiLimit.info = {
    defLimitData: {"biQuota": "", "dayQuota": "", "monthQuota": "", "yearQuota": ""},

    init: function () {
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity, width: '100%'});

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
            _terminalNo.append($("<option>").attr({"value": "00000000"}).html("-全部-"));
            if ($(this).val() != '') {
                ucf.ajax.commonFn(ctx + "/terminal/map", {"merchantNo": $(this).val()}, function (data) {
                    $.map(data.message, function (value, key) {
                        var _option = $("<option>").attr({"value": key}).html(key + " (" + value + ") ");
                        _terminalNo.append(_option);
                    });
                });
            }
        });

        var _parentBusiCodeInfo = $("#parentBusiCodeInfo");
        var _busiCodeInfo = $("#busiCodeInfo");
        _parentBusiCodeInfo.on("change", function () {
            _busiCodeInfo.empty();
            _busiCodeInfo.append($("<option>").attr({"value": ""}).html("-请选择-"));
            _busiCodeInfo.val("").trigger("change");

            var _val = _parentBusiCodeInfo.val();
            merchantBusiLimit.util.getBusiLimit(_val, true);

            if (_val != '') {
                ucf.ajax.commonFn(ctx + "/busiInfo/getBusiByParentCode", {
                    "busiCode": _parentBusiCodeInfo.val(),
                    "status": "1"
                }, function (data) {
                    var _busiInfoData = data.message;
                    $.map(_busiInfoData, function (value, key) {
                        var _option = $("<option>").attr({"value": key}).html(value);
                        _busiCodeInfo.append(_option);
                    });
                });
            }
        });


        _busiCodeInfo.on("change", function () {
            merchantBusiLimit.util.getBusiLimit($(this).val(), true);
        });


        $("#frmEdit").validate({
            rules: {
                merchantNo: {
                    required: true
                },
                parentBusiCodeInfo: {
                    required: true
                },
                biQuota: {
                    //required: true,
                    isDecimal: true
                },
                dayQuota: {
                    //required: true,
                    isDecimal: true
                },
                monthQuota: {
                    //required: true,
                    isDecimal: true
                },
                yearQuota: {
                    //required: true,
                    isDecimal: true
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
                if ($("#_method").val() == 'post') {
                    var _busiCode = $("#busiCode");
                    _busiCodeInfo.val() == '' ? _busiCode.val(_parentBusiCodeInfo.val()) : _busiCode.val(_busiCodeInfo.val());
                }
                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                    if (result.status == 'OK') {
                        merchantBusiLimit.list.getRecords();
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

        if ($("#_method").val() == 'put') {
            merchantBusiLimit.util.getBusiLimit($("input[name='busiCode']").val(), false);
        }

        $("#saveBtn").on("click", function () {
            $("#frmEdit").submit();
        });

        $("#delBtn").on("click", function () {
            var _param = {
                "_method": "delete",
                "merchantNo": $("input[name='merchantNo']").val(),
                "busiCode": $("input[name='busiCode']").val(),
                "terminalNo": $("input[name='terminalNo']").val()
            };
            confirm_dialog("操作确认", "确定删除吗？删除后数据不可恢复！", function (notice, val) {
                if (notice) {
                    ucf.ajax.commonFn($('#frmEdit').attr('action'), _param, function (result) {
                        if (result.status == 'OK') {
                            merchantBusiLimit.list.getRecords();
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
    }
};


merchantBusiLimit.util = {
    resetBusiLimit: function () {
        $("#biQuota").val("").rules('remove', 'max');
        $("#dayQuota").val("").rules('remove', 'max');
        $("#monthQuota").val("").rules('remove', 'max');
        $("#yearQuota").val("").rules('remove', 'max');
    },

    getBusiLimit: function (busiCode, flag) {
        var _biQuota = $("#biQuota");
        var _dayQuota = $("#dayQuota");
        var _monthQuota = $("#monthQuota");
        var _yearQuota = $("#yearQuota");
        var _defBiQuotaVal = '', _defDayQuotaVal = '', _defMonthQuotaVal = '', _defYearQuotaVal = '';

        if (flag) this.resetBusiLimit();
        if (busiCode != "") {
            ucf.ajax.commonFn(ctx + "/busiInfo/getBusiInfo", {"busiCode": busiCode}, function (data) {
                if (data.status == 'OK') {
                    var _busiInfoData = data.message;
                    _defBiQuotaVal = ucf.common.formatNum(_busiInfoData.limitRuleVo.biQuota, 2);
                    _defDayQuotaVal = ucf.common.formatNum(_busiInfoData.limitRuleVo.dayQuota, 2);
                    _defMonthQuotaVal = ucf.common.formatNum(_busiInfoData.limitRuleVo.monthQuota, 2);
                    _defYearQuotaVal = ucf.common.formatNum(_busiInfoData.limitRuleVo.yearQuota, 2);
                    //alert(_defBiQuotaVal + "|" + _defDayQuotaVal + "|" + _defMonthQuotaVal + "|" + _defYearQuotaVal);

                    if (_defBiQuotaVal != '') {
                        _biQuota.rules('add', {
                            max: parseFloat(_defBiQuotaVal)
                        });
                    }

                    if (_defDayQuotaVal != '') {
                        _dayQuota.rules('add', {
                            max: parseFloat(_defDayQuotaVal)
                        });
                    }

                    if (_defMonthQuotaVal != '') {
                        _monthQuota.rules('add', {
                            max: parseFloat(_defMonthQuotaVal)
                        });
                    }

                    if (_defYearQuotaVal != '') {
                        _yearQuota.rules('add', {
                            max: parseFloat(_defYearQuotaVal)
                        });
                    }

                    if (flag) {
                        _biQuota.val(_defBiQuotaVal);
                        _dayQuota.val(_defDayQuotaVal);
                        _monthQuota.val(_defMonthQuotaVal);
                        _yearQuota.val(_defYearQuotaVal);
                    }
                }
            });


        }
    }
};
