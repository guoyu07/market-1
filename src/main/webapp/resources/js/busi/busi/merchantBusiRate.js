if (typeof merchantBusiRate == 'undefined') {
    merchantBusiRate = {}
}

merchantBusiRate.list = {
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
            merchantBusiRate.list.qryPageData();
            return false;
        });
        merchantBusiRate.list.qryPageData();
    },


    qryPageData: function () {
        merchantBusiRate.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: merchantBusiRate.list.PageCallback,
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
        ucf.ajax.commonFn(ctx + "/merchantBusiRate/list", $("#frmQry").serialize(), function (result) {
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
                            "href": ctx + "/merchantBusiRate/" + obj.merchantNo + "/" + obj.busiCode + "/" + obj.terminalNo + ucf.common.getUrlRandom(),
                            "data-toggle": "modal",
                            "data-target": "#myModal"
                        }).html(obj.merchantNo + " | " + obj.merchantName)).appendTo(_tr);
                        $("<td>").html(obj.busiCode + " | " + obj.busiName).appendTo(_tr);
                        $("<td>").html(obj.terminalNameLabel).appendTo(_tr);
                        $("<td>").html(merchantBusiRate.util.rateAnalysis(obj).nowRun).appendTo(_tr);
                        $("<td>").html(merchantBusiRate.util.rateAnalysis(obj).futureRun).appendTo(_tr);
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
        merchantBusiRate.list.getRecords();
    }
};


merchantBusiRate.info = {
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
                merchantBusiRate.util.getDefRateVal(_val);
            }
        });


        _busiCodeInfo.on("change", function () {
            merchantBusiRate.util.getDefRateVal($(this).val());
        });


        $("#futureRateType").change(function () {
            if ($(this).val() == 1) {
                $(this).parent().next().html("费率(%):");
                $("#futureRateValue").attr("placeholder", "1%输入1");
            } else if ($(this).val() == 2) {
                $(this).parent().next().html("金额(元):");
                $("#futureRateValue").attr("placeholder", "具体的金额");
            } else {
                $(this).parent().next().html("费率:");
                $("#futureRateValue").attr("placeholder", "对应费率类型");
            }
        });


        $(".dateSelector").click(function () {
            WdatePicker({minDate: ucf.common.getDate("")});
        });

        $("#frmEdit").validate({
            rules: {
                merchantNo: {
                    required: true
                },
                parentBusiCodeInfo: {
                    required: true
                },
                futureRateType: {
                    required: true
                },
                futureRateValue: {
                    required: true,
                    isDecimal: true
                },
                futureMinAmount: {
                    min: 0,
                    isDecimal: true
                },
                futureMaxAmount: {
                    min: 0,
                    isDecimal: true,
                    twoNumVerify: function () {
                        return {"num": $("input[name='futureMinAmount']").val(), "type": "L"};
                    }
                },
                effectTime: {
                    required: true
                }
            },
            messages: {
                futureMaxAmount: {
                    "twoNumVerify": "封顶金额必须大于保底金额"
                }
            },
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
                        merchantBusiRate.list.getRecords();
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

    }
};

merchantBusiRate.util = {

    resetBusiRate: function () {
        $("#futureRateType").val("").trigger("change");
        $("#futureRateValue").val("");
        $("#futureMinAmount").val("");
        $("#futureMaxAmount").val("");
    },

    getDefRateVal: function (busiCode) {
        this.resetBusiRate();
        var _futureRateType = $("#futureRateType");
        var _futureRateValue = $("#futureRateValue");
        var _futureMinAmount = $("#futureMinAmount");
        var _futureMaxAmount = $("#futureMaxAmount");
        var _defFutureRateTypeVal = '', _defFutureRateValueVal = '', _defFutureMinAmountVal = '', _defFutureMaxAmountVal = '';
        if (busiCode != '') {
            ucf.ajax.commonFn(ctx + "/busiInfo/queryBusiRateInfo", {
                "busiCode": busiCode
            }, function (data) {
                var _busiRateInfoData = data.message;
                _defFutureRateTypeVal = _busiRateInfoData.rateType;
                _defFutureRateValueVal = ucf.common.formatNum(_busiRateInfoData.rateValue, 2);
                _defFutureMinAmountVal = ucf.common.formatNum(_busiRateInfoData.minAmount, 2);
                _defFutureMaxAmountVal = ucf.common.formatNum(_busiRateInfoData.maxAmount, 2);
                _futureRateType.val(_defFutureRateTypeVal).trigger("change");
                _futureRateValue.val(_defFutureRateValueVal);
                _futureMinAmount.val(_defFutureMinAmountVal);
                _futureMaxAmount.val(_defFutureMaxAmountVal);
            });
        }
    },

    rateAnalysis: function (obj) {
        var _nrRateType = "", _nrRateTypeLabel = "", _nrRateVal = "", _nrMinAmount = "", _nrMaxAmount = "";
        var _frRateType = "", _frRateTypeLabel = "", _frRateVal = "", _frMinAmount = "", _frMaxAmount = "", _frTime = '';
        if ("Y" == obj.effectFlag) {
            _nrRateTypeLabel = obj.futureRateTypeLabel;
            _nrRateType = obj.futureRateType;
            _nrRateVal = obj.futureRateValue;
            _nrMinAmount = obj.futureMinAmount;
            _nrMaxAmount = obj.futureMaxAmount;
            _frRateTypeLabel = "";
            _frRateType = "";
            _frRateVal = "";
            _frMinAmount = "";
            _frMaxAmount = "";
            _frTime = '';
        } else {
            _nrRateTypeLabel = obj.rateTypeLabel;
            _nrRateType = obj.rateType;
            _nrRateVal = obj.rateValue;
            _nrMinAmount = obj.minAmount;
            _nrMaxAmount = obj.maxAmount;
            _frRateTypeLabel = obj.futureRateTypeLabel;
            _frRateType = obj.futureRateType;
            _frRateVal = obj.futureRateValue;
            _frMinAmount = obj.futureMinAmount;
            _frMaxAmount = obj.futureMaxAmount;
            _frTime = obj.effectTimeLabel;
        }
        var _nowRunMsg = "费率类型: " + _nrRateTypeLabel + "<br>" +
            "费率: " + this.rateUnitFormat(_nrRateType, _nrRateVal) + "<br>" +
            "保底: " + this.amountUnitFormat(ucf.common.formatNum(_nrMinAmount, 2)) + "<br>" +
            "封顶: " + this.amountUnitFormat(ucf.common.formatNum(_nrMaxAmount, 2));


        var _futureRunMsg = "费率类型: " + _frRateTypeLabel + "<br>" +
            "费率: " + this.rateUnitFormat(_frRateType, _frRateVal) + "<br>" +
            "保底: " + this.amountUnitFormat(ucf.common.formatNum(_frMinAmount, 2)) + "<br>" +
            "封顶: " + this.amountUnitFormat(ucf.common.formatNum(_frMaxAmount, 2)) + "<br>" +
            "生效时间: " + _frTime;

        return {"nowRun": _nowRunMsg, "futureRun": _futureRunMsg};
    },


    rateUnitFormat: function (rateType, rateVal) {
        if (rateType == "1") {
            return rateVal + "%";
        } else if (rateType == "2") {
            return rateVal + "元";
        } else {
            return "";
        }
    },

    amountUnitFormat: function (val) {
        return val == "" ? "" : val + "元"
    }
};