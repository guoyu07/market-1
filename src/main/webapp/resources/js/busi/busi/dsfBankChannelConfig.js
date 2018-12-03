if (typeof dsfBankChannelConfig == 'undefined') {
    dsfBankChannelConfig = {};
}

dsfBankChannelConfig.list = {
    init: function () {
        dsfBankChannelConfig.list.qryPageData();

        $("select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            dsfBankChannelConfig.list.qryPageData();
        });
        $("#frmQry").on("submit", function () {
            dsfBankChannelConfig.list.qryPageData();
            return false;
        });

        var _beginDate = ucf.common.getDate("1", 'yyyy-MM-dd');
        var _endDate = ucf.common.getDate("", 'yyyy-MM-dd');
        $("input[name='beginDate']").val(_beginDate);
        $("input[name='endDate']").val(_endDate);
        $(".dateSelector").click(function () {
            WdatePicker();
        });


        $("#channelNo").select2({
            width: "100%",
            language: "zh-CN",
            placeholder: "输入通道编号或者通道名称",
            cache: "true",
            allowClear: true,
            ajax: {
                url: ctx + "/dsfChannel/map",
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


    },

    qryPageData: function () {
        dsfBankChannelConfig.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: dsfBankChannelConfig.list.PageCallback,
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
        ucf.ajax.commonFn(ctx + "/dsfBankChannelConfig/list", $("#frmQry").serialize(), function (result) {
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
                        $("<td>").html(obj.id).appendTo(_tr);
                        $("<td>").html(obj.bankName).appendTo(_tr);
                        $("<td>").html(obj.channelName).appendTo(_tr);
                        $("<td>").html(obj.conditionLabel).appendTo(_tr);
                        $("<td>").html(obj.appTime).appendTo(_tr);
                        $("<td>").html(obj.appOpno).appendTo(_tr);
                        $("<td>").html($("<a>").attr({"href": "javascript:void(0)"}).html("删除").on("click", function () {
                            var _param = {"_method": "delete", "id": obj.id};
                            confirm_dialog("操作确认", "确定删除吗？删除后数据不可恢复！", function (notice, val) {
                                if (notice) {
                                    ucf.ajax.commonFn(ctx + '/dsfBankChannelConfig', _param, function (result) {
                                        dsfBankChannelConfig.list.getRecords();
                                        $.pnotify({title: result.message, type: result.status});
                                    });
                                }
                            });
                        })).appendTo(_tr);
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
        dsfBankChannelConfig.list.getRecords();
    }
};

dsfBankChannelConfig.info = {
    init: function () {
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("#frmEdit").validate({
            rules: {
                bankNo: {
                    required: true
                },
                channelNo: {
                    required: true,
                    remote: {
                        type: "post",
                        url: ctx + "/dsfBankChannelConfig/check",
                        data: {
                            "bankNo": function () {
                                return $("#bankNo").val();
                            },
                            "channelNo": function () {
                                return $("#channelNo").val();
                            }
                        },
                        dataFilter: function (result, type) {
                            var _result = eval('(' + result + ')');
                            return (_result.message);
                        }
                    }
                },
                condition: {
                    required: true
                }
            },
            messages: {
                channelNo: {
                    remote: "该配置已经存在，不能重复添加"
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
                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                    if (result.status == 'OK') {
                        dsfBankChannelConfig.list.getRecords();
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

    }
};
dsfBankChannelConfig.verify = {
    addVerify: function () {

    }
};

dsfBankChannelConfig.util = {};