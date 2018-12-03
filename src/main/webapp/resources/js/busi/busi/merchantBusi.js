if (typeof merchantBusi == 'undefined') {
    merchantBusi = {};
}

merchantBusi.list = {
    init: function () {
        $("select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            merchantBusi.list.qryPageData();
        });
        $("#frmQry").on("submit", function () {
            merchantBusi.list.qryPageData();
            return false;
        });
        $("#newBt").on("click", function () {
            window.open(ctx + "/merchantBusi/0/0" + ucf.common.getUrlRandom());
        });

        $("#merchantInfoTerm").select2({
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
        });


        $("#busiInfoTerm").select2({
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

        merchantBusi.list.qryPageData();
    },


    qryPageData: function () {
        merchantBusi.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: merchantBusi.list.PageCallback,
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
        ucf.ajax.commonFn(ctx + "/merchantBusi/list", $("#frmQry").serialize(), function (result) {
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
                        $("<td>").append($("<a>").attr({"href": "javascript:void(0)"}).on("click", function () {
                            window.open(ctx + "/merchantBusi/" + obj.merchantNo + "/" + obj.busiCode + ucf.common.getUrlRandom());
                        }).html(obj.merchantNo + " | " + obj.merchantName)).appendTo(_tr);
                        $("<td>").html(obj.busiCode + " | " + obj.busiName).appendTo(_tr);
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
        merchantBusi.list.getRecords();
    },

    setQueryCondition: function (param) {
        //alert(param.merchantInfoVal + "|" + param.merchantInfoText);
        var _merchantInfoTerm = $("#merchantInfoTerm");
        _merchantInfoTerm.append($("<option>").attr({"value": param.merchantInfoVal}).html(param.merchantInfoText));
        _merchantInfoTerm.val(param.merchantInfoVal).trigger("change");
    }

};

merchantBusi.info = {
    init: function (param) {
        $("#merchantBusiForm select").select2({minimumResultsForSearch: Infinity, width: '100%'});
        $('input').iCheck('destroy');

        $(".dateSelector").click(function () {
            WdatePicker({minDate: ucf.common.getDate("")});
        });

        $("#closeBt").on("click", function () {
            window.opener = null;
            window.close();
        });
        merchantBusi.verify.addVerify();
        this.formSubmit();

        this.merchant.init(param);
        this.busiCode.init(param);
        this.wg.init(param);
        this.reward.init(param);
        this.terminal.init(param);
    },


    /*
     商户信息
     */
    merchant: {
        init: function () {
            $("#merchantInfo").select2({
                width: "100%",
                language: "zh-CN",
                placeholder: "输入商户账号或者商户名称",
                cache: "true",
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
                merchantBusi.info.busiCode.parentBusiCodeReset();
                merchantBusi.info.terminal.getMerchantTerminalList($(this).val());
            });


        }
    },

    /*
     业务信息
     */
    busiCode: {
        init: function (param) {
            $("#parentBusiCodeInfo").on("change", function () {
                merchantBusi.info.busiCode.queryBusiCodeByParentBusi($(this).val());
                merchantBusi.info.wg.checkWgBusi($(this).val());
            });
        },

        queryBusiCodeByParentBusi: function (parentBusiCode) {
            merchantBusi.info.busiCode.busiCodeReset();
            if (parentBusiCode != '') {
                var _busiCodeInfo = $("#busiCodeInfo");
                ucf.ajax.commonFn(ctx + "/busiInfo/getBusiByParentCode", {"busiCode": parentBusiCode}, function (data) {
                    $.each(data.message, function (key, value) {
                        var _option = $("<option>").attr({"value": key}).html(value);
                        _busiCodeInfo.append(_option);
                    });
                });
            }
        },

        parentBusiCodeReset: function () {
            $("#parentBusiCodeInfo").val("").trigger("change");
        },

        busiCodeReset: function () {
            var _busiCodeInfo = $("#busiCodeInfo");
            _busiCodeInfo.empty();//移除所有的数据行
            _busiCodeInfo.append($("<option>").attr({"value": ""}).html("-请选择-"));
            _busiCodeInfo.val("").trigger("change");
        }
    },

    /*
     网关信息
     */
    wg: {
        init: function (param) {
            $("#customFlag").on("change", function () {
                if ($(this).val() == '0') {
                    $("#wgCustomCode").val("").trigger("change");
                    $("#eCommerceCode").val("");
                }
            });
        },

        checkWgBusi: function (busiCode) {
            merchantBusi.info.wg.wgMerchantReset(false);
            if ("B001" == busiCode) {
                ucf.ajax.commonFn(ctx + "/merchantBusi/checkMerchantBusi", {
                    "merchantNo": $("#merchantInfo").val(),
                    "busiCode": busiCode,
                    "terminalNo": '',
                    "type": "wg"
                }, function (data) {
                    if (data.message) {
                        merchantBusi.info.wg.wgMerchantReset(true);
                    } else {
                        merchantBusi.info.wg.wgMerchantReset(false);
                    }
                });
            }
        },

        wgMerchantReset: function (flag) {
            var _payGetwayBusiDetail = $("#payGetwayBusiDetail");
            if (flag) {
                _payGetwayBusiDetail.show();
            } else {
                _payGetwayBusiDetail.hide();
            }
        }
    },

    /*
     商户返佣信息
     */
    reward: {
        init: function (param) {
            var _rewardDataListDiv = $("#rewardDiv");
            var _addRewardDataBt = $("#addRewardBtn");
            var _rewardDivTitle = $("#rewardDivTitle");
            var _standType = $("#standType");
            _standType.on("change", function () {
                _rewardDataListDiv.empty();
                if ($(this).val() == '') {
                    _rewardDivTitle.hide();
                    _addRewardDataBt.hide();
                } else {
                    _rewardDivTitle.show();
                    _addRewardDataBt.show();
                    _rewardDataListDiv.append(merchantBusi.info.reward.getRewardTemplate(0));
                    merchantBusi.verify.addRewardDataVerify(0);
                }
            });

            _addRewardDataBt.on("click", function () {
                var _index = parseInt($(".rewardDataDiv").length);
                _rewardDataListDiv.append(merchantBusi.info.reward.getRewardTemplate(_index));
                merchantBusi.verify.addRewardDataVerify(_index);
                if (_rewardDataListDiv.children().length == 5) {
                    $(this).hide();
                }
            });

            $(".delRewardBtn").on("click", function () {
                merchantBusi.info.reward.rewardDivDel($(this));
            });


            if ($("#_method").val() == "put" && _standType.val() != '') {
                for (var i = 0; i < _rewardDataListDiv.children().length; i++) {
                    merchantBusi.verify.addRewardDataVerify(i);
                }
            }
        },

        getRewardTemplate: function (index) {
            var _rewardHtml = $("<div>").attr({class: 'form-group rewardDataDiv'});
            var _div1 = $("<div>").attr({class: 'col-sm-2'});
            var _div2 = $("<div>").attr({class: 'col-sm-2'}).
            append($("<input>").attr({
                id: "standValue_" + index,
                name: "standValue_" + index,
                type: "text",
                class: "form-control",
                autocomplete: "off",
                placeholder: "达标条件"
            }));
            var _div3 = $("<div>").attr({class: 'col-sm-2'}).
            append($("<input>").attr({
                id: "rewardValue_" + index,
                name: "rewardValue_" + index,
                type: "text",
                class: "form-control",
                autocomplete: "off",
                placeholder: "(如1%则填1)"
            }));
            var _div4 = $("<div>").attr({class: 'col-sm-2'}).
            append($("<input>").attr({
                id: "rewardMinAmount_" + index,
                name: "rewardMinAmount_" + index,
                value: "0.00",
                type: "text",
                class: "form-control",
                autocomplete: "off",
                placeholder: "元"
            }));
            var _div5 = $("<div>").attr({class: 'col-sm-2'}).
            append($("<input>").attr({
                id: "rewardMaxAmount_" + index,
                name: "rewardMaxAmount_" + index,
                value: "",
                type: "text",
                class: "form-control",
                autocomplete: "off",
                placeholder: "元"
            }));
            var _div6 = $("<div>").attr({class: 'col-sm-1'}).append(index == 0 ? "" : $("<a>").attr({href: "javascript:void(0);"}).text("删除").click(function () {
                merchantBusi.info.reward.rewardDivDel($(this));
            }));
            return _rewardHtml.append(_div1).append(_div2).append(_div3).append(_div4).append(_div5).append(_div6);
        },

        rewardDivDel: function (obj) {
            obj.parent().parent().remove();
            if ($("#rewardDiv").children().length < 5) {
                $("#addRewardBtn").show();
            }
        },

        changeVal: function () {
            var _length = $("#rewardDiv").children().length;
            var _standValue = "", _rewardValue = "", _rewardMinAmount = "", _rewardMaxAmount = "";
            for (var i = 0; i < _length; i++) {
                _standValue = _standValue + $("#standValue_" + i).val() + (i + 1 < _length ? "," : "");
                _rewardValue = _rewardValue + $("#rewardValue_" + i).val() + (i + 1 < _length ? "," : "");
                _rewardMinAmount = _rewardMinAmount + $("#rewardMinAmount_" + i).val() + (i + 1 < _length ? "," : "");
                _rewardMaxAmount = _rewardMaxAmount + $("#rewardMaxAmount_" + i).val() + (i + 1 < _length ? "," : "");
            }
            //alert(_standValue + "|" + _rewardValue + "|" + _rewardMinAmount + "|" + _rewardMaxAmount);
            $("#standValue").val(_standValue);
            $("#rewardValue").val(_rewardValue);
            $("#rewardMinAmount").val(_rewardMinAmount);
            $("#rewardMaxAmount").val(_rewardMaxAmount);
        }
    },

    /*
     终端设置
     */
    terminal: {
        init: function (param) {
            var _allTerminalBt = $("#allTerminalBt");
            _allTerminalBt.on("click", function () {
                if ($(this).prop('checked')) {
                    $("input[name='terminalInfo']").prop('checked', true);
                } else {
                    $("input[name='terminalInfo']").prop('checked', false);
                }
            });

            $("input[name='terminalInfo']").on("click", function () {
                if (merchantBusi.info.terminal.allTerminalCheckFlag()) {
                    _allTerminalBt.prop('checked', true)
                } else {
                    _allTerminalBt.prop('checked', false)
                }
            });


            if (merchantBusi.info.terminal.allTerminalCheckFlag()) {
                _allTerminalBt.prop('checked', true)
            } else {
                _allTerminalBt.prop('checked', false)
            }
        },

        allTerminalCheckFlag: function () {
            var _terminalInfo = $("input[name='terminalInfo']");
            if (_terminalInfo.length == 0)return false;
            var _allFlag = true;
            $.each(_terminalInfo, function (i, d) {
                if (!$(d).prop('checked')) {
                    _allFlag = false;
                    return false;
                }
            });
            return _allFlag;
        },

        makeTerminalInfoTr: function (terminalData) {
            var _terminalInfoDiv = $("#terminalInfoDiv");
            var _i = 0;
            $.each(terminalData, function (key, value) {
                var _tr = $("<tr>");
                var _td1 = $("<td>");
                var _td2 = $("<td>");
                var _td3 = $("<td>");
                _td1.html(key).appendTo(_tr);
                _td2.html(value).appendTo(_tr);
                _td3.append($("<input>").attr({
                    "type": "checkbox",
                    "class": "terminalInfoBox",
                    "name": "terminalInfo",
                    "id": "terminalInfo_" + key,
                    "value": key
                }).on("click", function () {
                    var _allTerminalBt = $("#allTerminalBt");
                    if (merchantBusi.info.terminal.allTerminalCheckFlag()) {
                        _allTerminalBt.prop('checked', true)
                    } else {
                        _allTerminalBt.prop('checked', false)
                    }
                })).appendTo(_tr);
                _terminalInfoDiv.append(_tr);
                _i++;
            });
            if (_i > 0) {
                merchantBusi.info.terminal.allTerminalBtDivReset(true);
            }
        },

        getMerchantTerminalList: function (merchantNo) {
            $("#terminalInfoDiv").empty();
            merchantBusi.info.terminal.allTerminalBtDivReset(false);
            if (merchantNo != '') {
                ucf.ajax.commonFn(ctx + "/terminal/map", {"merchantNo": merchantNo}, function (data) {
                    merchantBusi.info.terminal.makeTerminalInfoTr(data.message);
                });
            }
        },

        allTerminalBtDivReset: function (flag) {
            var _allTerminalBtDiv = $("#allTerminalBtDiv");
            if (flag) {
                _allTerminalBtDiv.show();
            } else {
                _allTerminalBtDiv.hide();
            }
        }


    },

    /*
     提交表单和验证
     */
    formSubmit: function () {
        var _merchantInfo = $("#merchantInfo");
        var _parentBusiCodeInfo = $("#parentBusiCodeInfo");
        var _busiCodeInfo = $("#busiCodeInfo");

        $("#merchantBusiForm").validate({
            "rules": {
                merchantInfo: {
                    required: true
                },
                terminalInfo: {
                    required: true
                },
                parentBusiCodeInfo: {
                    required: function () {
                        return _busiCodeInfo.find("option").length == 1;
                    },
                    busiCodeVerify: function () {
                        return {
                            "merchantInfo": _merchantInfo,
                            "parentBusiCodeInfo": _parentBusiCodeInfo,
                            "busiCodeInfo": _busiCodeInfo,
                            "utilFn": ucf.ajax
                        };
                    }
                },
                busiCodeInfo: {
                    required: function () {
                        return _busiCodeInfo.find("option").length != 1;
                    },
                    busiCodeVerify: function () {
                        return {
                            "merchantInfo": _merchantInfo,
                            "parentBusiCodeInfo": _parentBusiCodeInfo,
                            "busiCodeInfo": _busiCodeInfo,
                            "utilFn": ucf.ajax
                        };
                    }

                },
                busiDesc: {
                    rangelength: [1, 100]
                },

                cycleValue: {
                    required: true
                },

                url: {
                    required: function () {
                        return $("#payGetwayBusiDetail").is(":visible");
                    },
                    wgUrlVerify: true,
                    maxlength: 400
                },
                ipAreaType: {
                    required: function () {
                        return $("#payGetwayBusiDetail").is(":visible");
                    }
                },
                payType: {
                    required: function () {
                        return $("#payGetwayBusiDetail").is(":visible");
                    }
                },
                defaultPay: {
                    required: function () {
                        return $("#payGetwayBusiDetail").is(":visible");
                    }
                },
                wgCustomCode: {
                    required: function () {
                        return $("#customFlag").val() == '1';
                    }
                },

                eCommerceCode: {
                    required: function () {
                        return $("#customFlag").val() == '1';
                    },
                    maxLengthVerify: 30
                },
                standType: {
                    //required: true
                }
            },
            messages: {
                "parentBusiCodeInfo": {
                    "remote": "该商户已经开通过此业务"
                },
                "busiCodeInfo": {
                    "remote": "该商户已经开通过此业务"
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
            submitHandler: function () {
                var _frmEdit = $("#merchantBusiForm");
                var _saveBtn = $("#merchantBusiSaveBtn");
                merchantBusi.info.reward.changeVal();
                ucf.ajax.commonFn(_frmEdit.attr('action'), _frmEdit.serialize(), function (result) {
                    if (result.status == 'OK') {
                        _saveBtn.hide();
                        $.pnotify({title: result.message, type: result.status});
                        if (window.opener != null) {
                            window.opener.merchantBusi.list.setQueryCondition(merchantBusi.util.getQueryCondition());
                            window.opener.merchantBusi.list.getRecords();
                        }
                        setTimeout(function () {
                            $("#closeBt").click();
                        }, 1000);
                    } else {
                        _saveBtn.attr("data-content", result.message);
                        _saveBtn.popover('toggle');
                        setTimeout(function () {
                            _saveBtn.popover('toggle');
                        }, 2000);
                    }
                });
            }
        });
    }
};


merchantBusi.verify = {
    addRewardDataVerify: function (index) {
        $("input[name=standValue_" + index + "]").rules('add', {
            required: true,
            min: 0,
            digits: true,
            rewardDataValVerify: function () {
                return {"last": index == 0 ? "" : $("input[name=standValue_" + (parseInt(index) - 1) + "]").val()};
            }
        });
        $("input[name=rewardValue_" + index + "]").rules('add', {
            required: true,
            min: 0,
            isDecimal: true
        });
        $("input[name=rewardMinAmount_" + index + "]").rules('add', {
            required: true,
            min: 0,
            isDecimal: true
        });
        $("input[name=rewardMaxAmount_" + index + "]").rules('add', {
            min: 0.01,
            isDecimal: true,
            twoNumVerify: function () {
                return {"num": $("input[name=rewardMinAmount_" + index + "]").val(), "type": "L"};
            },
            messages: {
                twoNumVerify: "保底不能大于封顶"
            }
        });
    },

    addVerify: function () {
        jQuery.validator.addMethod("rewardDataValVerify", function (value, element, param) {
            var _lastVal = param.last;
            var _flag = true;
            //alert(_lastVal + "|" + value);
            if (_lastVal != '' && parseFloat(value) <= parseFloat(_lastVal)) {
                _flag = false;
            }
            return _flag;
        }, "该值应该大于上一行的值");

        jQuery.validator.addMethod("wgUrlVerify", function (value, element, param) {
            if (value == '')return true;
            var _t = /^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/;
            var _urls = value.split(";");
            var _flag = true;
            $.each(_urls, function (i, n) {
                //alert(n + "|" + _t.test(n));
                if (!_t.test(n)) {
                    _flag = false;
                    return false;
                }
            });
            return _flag;
        }, "请输入正确URL信息");

        jQuery.validator.addMethod("busiCodeVerify", function (value, element, param) {
            var _now = $(element);
            var _utilFn = param.utilFn;
            var _mi = param.merchantInfo;
            var _pbc = param.parentBusiCodeInfo;
            var _bc = param.busiCodeInfo;
            var _c = '';

            var _checkFlag = true;
            if (_now.attr("id") == 'parentBusiCodeInfo' && _pbc.val() != '' && _bc.val() == '') {
                _c = _pbc.val();
            } else if (_now.attr("id") == 'busiCodeInfo' && _pbc.val() != '' && _bc.val() != '') {
                _c = _bc.val();
            } else {
                return _checkFlag;
            }

            if (_c != '') {
                _utilFn.commonFn(ctx + "/merchantBusi/checkMerchantBusi", {
                    "merchantNo": _mi.val(),
                    "busiCode": _c
                }, function (data) {
                    _checkFlag = data.message;
                });
                return _checkFlag;
            }
        }, "该商户已经开通过此业务");

        /*jQuery.validator.addMethod("effectTimeVerify", function (value, element, param) {
         var _futureRateType = param.futureRateType;
         if (_futureRateType.val() != '' && value != '') {
         value = value.replace(/-/g, '');
         if (value.length != 8)return false;
         var _e = new Date(value.substr(0, 4), value.substr(4, 2) - 1, value.substr(6, 2));
         var _nowDate = new Date();
         var _days = (_e.getTime() - _nowDate.getTime()) / 24 / 60 / 60 / 1000;
         alert(_days);
         if (_days < 0) {
         return false;
         }
         }
         return true;
         }, "生效时间格式不对，生效时间不能早于当前时间");*/
    }
};


merchantBusi.util = {
    formItemCopy: function (sourceForm, param, targerForm) {
        $.each(param, function (key, value) {
            targerForm.find("input[name=" + param[key].attr("name") + "]").remove();
            var _emptyObj = $("<input>").attr({
                "type": "hidden",
                "name": param[key].attr("name"),
                "value": param[key].val()
            });
            _emptyObj.appendTo(targerForm);
        });
    },

    getQueryCondition: function () {
        var _method = $("#_method");
        var _param = {};
        if ("post" == _method.val()) {
            _param = {
                "merchantInfoVal": $("select[name='merchantInfo']").val(),
                "merchantInfoText": $("select[name='merchantInfo']").find("option:selected").text()
            }
        } else if ("put" == _method.val()) {
            _param = {
                "merchantInfoVal": $("input[name='merchantInfo']").val(),
                "merchantInfoText": $("input[name='_merchantName']").val()
            }
        }
        return _param;
    }
};