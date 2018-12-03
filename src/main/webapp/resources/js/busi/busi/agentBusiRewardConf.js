if (typeof agentBusiRewardConf == 'undefined') {
    agentBusiRewardConf = {};
}

agentBusiRewardConf.list = {
    init: function () {
        $("select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        var _beginDate = ucf.common.getDate("1", 'yyyy-MM-dd');
        var _endDate = ucf.common.getDate("", 'yyyy-MM-dd');
        $("input[name='beginDate']").val(_beginDate);
        $("input[name='endDate']").val(_endDate);
        $(".dateSelector").click(function () {
            WdatePicker();
        });


        $("#merchantNo").select2({
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
            agentBusiRewardConf.list.qryPageData();
        });

        $("#frmQry").on("submit", function () {
            agentBusiRewardConf.list.qryPageData();
            return false;
        });

        agentBusiRewardConf.list.qryPageData();
    },


    qryPageData: function () {
        agentBusiRewardConf.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: agentBusiRewardConf.list.PageCallback,
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
        ucf.ajax.commonFn(ctx + "/agentBusiRewardConf/list", $("#frmQry").serialize(), function (result) {
            if (result.status == 'OK') {
                totalElements = result.message.totalElements;
                $('#tblData tbody:eq(0)').empty();//移除所有的数据行
                var _noRecord = $("<tr>").append($("<td>").attr({
                    "colspan": $("#tblData tr th").length,
                    "style": "text-align: center"
                }).html("没有搜索到数据"));
                if (typeof result.message.content == "undefined" || result.message.content.length == 0) {
                    $("#tblData tbody:eq(0)").append(_noRecord);
                } else {
                    $.each(result.message.content, function (index, obj) {
                        var _tr = $("<tr>");
                        $("<td>").html($("<a>").attr({
                            "href": ctx + "/agentBusiRewardConf/" + obj.merchantNo + "/" + obj.busiCode + ucf.common.getUrlRandom(),
                            "data-toggle": "modal",
                            "data-target": "#myModal"
                        }).html(obj.merchantNo + "|" + obj.merchantName)).appendTo(_tr);
                        $("<td>").html(obj.busiName).appendTo(_tr);
                        $("<td>").html(obj.standTypeLabel).appendTo(_tr);
                        $("<td>").html(agentBusiRewardConf.list.displayRewardDate(obj.standType, obj.rewardLevelVos, index)).appendTo(_tr);
                        $("<td>").html(obj.appTime).appendTo(_tr);
                        $("#tblData tbody:eq(0)").append(_tr);
                    });
                }
            } else {
                $.pnotify({title: result.message, type: 'error'});
            }
        });
    },

    PageCallback: function (pageIndex, jq) {
        $("input[name='page']").val(pageIndex);
        agentBusiRewardConf.list.getRecords();
    },


    displayRewardDate: function (standType, data, index) {
        var _t = $("<table>").attr("class", "table table-bordered table-hover dataTable");
        var _trTitle = $("<tr>");
        _trTitle.append($("<td>").html("达标条件"));
        _trTitle.append($("<td>").html("返佣费率"));
        _trTitle.append($("<td>").html("保底"));
        _trTitle.append($("<td>").html("封顶"));
        _t.append($("<thead>").append(_trTitle));


        var _tBody = $("<tbody>");
        $.each(data, function (i, e) {
            var _trDate = $("<tr>");
            var _v = "";
            if (standType == '1') {
                _v = ucf.common.formatNum(e.standValue, 2) + " 元";
            } else if (standType == '2') {
                _v = e.standValue.split(".")[0] + " 笔";
            } else if (standType == '3') {
                _v = e.standValue + " %";
            }
            _trDate.append($("<td>").html(_v));
            _trDate.append($("<td>").html(e.rewardValue + " %"));
            _trDate.append($("<td>").html(ucf.common.formatNum(e.minAmount, 2) + " 元"));
            _trDate.append($("<td>").html(agentBusiRewardConf.list.formatVal(e.maxAmount)));
            _tBody.append(_trDate);
        });
        return _t.append(_tBody);
    },


    formatVal: function (d) {
        return d == "" ? "无封顶" : ucf.common.formatNum(d, 2) + " 元";
    }
};

agentBusiRewardConf.info = {
    init: function () {
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("#saveBtn").on("click", function () {
            $("#frmEdit").submit();
        });

        $("#delBtn").on("click", function () {
            agentBusiRewardConf.info.delObj();
        });

        var _merchantInfo = $("#merchantNo");
        var _busiCodeInfo = $("#busiCode");
        var _parentBusiCodeInfo = $("#parentBusiCode");
        var _standType = $("#standType");

        _merchantInfo.select2({
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


        _parentBusiCodeInfo.on("change", function () {
            _busiCodeInfo.empty();//移除所有的数据行
            _busiCodeInfo.append($("<option>").attr({"value": ""}).html("-请选择-"));
            _busiCodeInfo.val("").trigger("change");
            var _val = $(this).val();
            if (_val != '') {
                ucf.ajax.commonFn(ctx + "/busiInfo/getBusiByParentCode", {"busiCode": _val}, function (data) {
                    var _i = 0;
                    $.map(data.message, function (value, key) {
                        var _option = $("<option>").attr({"value": key}).html(value);
                        _busiCodeInfo.append(_option);
                        _i++;
                    });
                });
            }
        });

        var _rewardDataListDiv = $("#rewardDataListDiv");
        var _addRewardDataBt = $("#addRewardDataBt");
        var _rewardDivTitle = $("#rewardDivTitle");
        _standType.on("change", function () {
            _rewardDataListDiv.empty();
            if ($(this).val() == '') {
                _rewardDivTitle.hide();
                _addRewardDataBt.hide();
            } else {
                _rewardDivTitle.show();
                _addRewardDataBt.show();
                _rewardDataListDiv.append(agentBusiRewardConf.info.getRewardTemplate(0));
                agentBusiRewardConf.verify.addRewardDataVerify(0, _standType.val());
            }
        });

        _addRewardDataBt.on("click", function () {
            var _index = parseInt($(".rewardDataDiv").length);
            _rewardDataListDiv.append(agentBusiRewardConf.info.getRewardTemplate(_index));
            agentBusiRewardConf.verify.addRewardDataVerify(_index, _standType.val());
            if (_rewardDataListDiv.children().length == 5) {
                $(this).hide();
            }
        });

        $(".delRewardBtn").on("click", function () {
            agentBusiRewardConf.info.rewardDivDel($(this));
        });

        $("#frmEdit").validate({
            "rules": {
                merchantNo: {
                    required: true
                },
                parentBusiCode: {
                    required: function () {
                        return _busiCodeInfo.find("option").length == 1;
                    }
                },
                memo: {},
                busiCode: {
                    required: function () {
                        return _busiCodeInfo.find("option").length != 1;
                    }

                },
                standType: {
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
            submitHandler: function () {
                var _frmEdit = $("#frmEdit");
                var _saveBtn = $("#saveBtn");
                agentBusiRewardConf.info.changeVal();
                ucf.ajax.commonFn(_frmEdit.attr('action'), _frmEdit.serialize(), function (result) {
                    if (result.status == 'OK') {
                        agentBusiRewardConf.list.getRecords();
                        $('[data-dismiss]').click();
                        $.pnotify({title: result.message, type: result.status});
                    } else {
                        _saveBtn.attr("data-content", result.message);
                        _saveBtn.popover('toggle');
                        setTimeout(function () {
                            $("#saveBtn").popover('toggle');
                        }, 2000);
                    }

                });
            }
        });

        if ($("#_method").val() == "put") {
            for (var i = 0; i < _rewardDataListDiv.children().length; i++) {
                agentBusiRewardConf.verify.addRewardDataVerify(i, _standType.val());
            }
        }
    },

    delObj: function () {
        var _param = {
            "_method": "delete",
            "merchantNo": $("input[name='merchantNo']").val(),
            "busiCode": $("input[name='busiCode']").val()
        };
        confirm_dialog("操作确认", "确定删除吗？删除后数据不可恢复！", function (notice, val) {
            if (notice) {
                ucf.ajax.commonFn($('#frmEdit').attr('action'), _param, function (result) {
                    if (result.status == 'OK') {
                        agentBusiRewardConf.list.getRecords();
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
    },

    getRewardTemplate: function (index) {
        var _rewardHtml = $("<div>").attr({class: 'form-group rewardDataDiv'});
        //var _div1 = $("<div>").attr({class:'col-sm-2'});
        var _div2 = $("<div>").attr({class: 'col-sm-3'}).
        append($("<input>").attr({
            id: "standValue_" + index,
            name: "standValue_" + index,
            type: "text",
            class: "form-control",
            autocomplete: "off",
            placeholder: "达标条件"
        }));
        var _div3 = $("<div>").attr({class: 'col-sm-3'}).
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
        var _div6 = $("<div>").attr({class: 'col-sm-2'}).append(index == 0 ? "" : $("<a>").attr({href: "javascript:void(0);"}).text("删除").click(function () {
            agentBusiRewardConf.info.rewardDivDel($(this));
        }));
        return _rewardHtml.append(_div2).append(_div3).append(_div4).append(_div5).append(_div6);
    },


    rewardDivDel: function (obj) {
        obj.parent().parent().remove();
        if ($("#rewardDataListDiv").children().length < 5) {
            $("#addRewardDataBt").show();
        }
    },


    changeVal: function () {
        //$.each($("input[name^='standValue']"), function (i, e) {
        //    $(e).attr("name", "standValue")
        //});
        //$.each($("input[name^='rewardValue']"), function (i, e) {
        //    $(e).attr("name", "rewardValue")
        //});
        //$.each($("input[name^='rewardMinAmount']"), function (i, e) {
        //    $(e).attr("name", "rewardMinAmount")
        //});
        //$.each($("input[name^='rewardMaxAmount']"), function (i, e) {
        //    $(e).attr("name", "rewardMaxAmount")
        //});

        var _length = $("#rewardDataListDiv").children().length;
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
};

agentBusiRewardConf.verify = {

    addRewardDataVerify: function (index, standType) {
        agentBusiRewardConf.verify.addValVerify();
        var _standValue = $("input[name=standValue_" + index + "]");
        _standValue.rules('add', {
            required: true,
            min: 0,
            rewardDataValVerify: function () {
                return {"last": index == 0 ? "" : $("input[name=standValue_" + (parseInt(index) - 1) + "]").val()};
            }
        });
        if (standType == '1' || standType == '3') {
            _standValue.rules('remove', 'digits');
            _standValue.rules('add', {
                isDecimal: true
            });
        } else if (standType == '2') {
            _standValue.rules('remove', 'isDecimal');
            _standValue.rules('add', {
                digits: true
            });
        }

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

    addValVerify: function () {
        jQuery.validator.addMethod("rewardDataValVerify", function (value, element, param) {
            var _lastVal = param.last;
            var _flag = true;
            //alert(_lastVal + "|" + value);
            if (_lastVal != '' && parseFloat(value) <= parseFloat(_lastVal)) {
                _flag = false;
            }
            return _flag;
        }, "该值应该大于上一行的值");
    }
};


