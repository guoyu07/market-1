if (typeof busi == 'undefined') {
    acqBatchMerchantBusi = {};
}
var totalElements = 0;
acqBatchMerchantBusi.list = {
    init: function () {
        $("select").select2({minimumResultsForSearch: Infinity, width: '100%'});
        $(".dateSelector").click(function () {
            WdatePicker();
        });

        acqBatchMerchantBusi.list.qryPageData();

        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            acqBatchMerchantBusi.list.qryPageData();
        });
        $("#frmQry").on("submit", function () {
            acqBatchMerchantBusi.list.qryPageData();
            return false;
        });

        $("#btnApply").click(function () {
            if ($("#xlsFileName").val() == "") {
                $.pnotify({title: "请上传申请文件", type: "error"});
                return;
            }
            var _params = {xlsFileName: $("#xlsFileName").val(), memo: $("#memo").val()};
            ucf.ajax.commonFn(ctx + "/acqBatchMerchantBusi/apply", _params, function (result) {
                $.pnotify({title: result.message, type: result.status});
                if (result.status == 'OK')
                    acqBatchMerchantBusi.list.qryPageData();
            });
        });

        $("#xlsFile").uploadifive({
            auto: true,
            multi: false,
            buttonText: "选择文件",
            fileType: 'application/vnd.ms-excel',
            formData: "",
            removeCompleted: false,
            removeTimeout: 1,
            'uploadScript': ctx + '/fileUpload/acqBatchBusiFile',
            'onUploadComplete': function (file, data) {
                var json = eval('(' + data + ')');
                if (json.status == 'OK') {
                    $("#xlsFileName").val(json.message);
                } else {
                    $.pnotify({title: json.message, type: json.status});
                }
            }
        });


    },
    qryPageData: function () {
        acqBatchMerchantBusi.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: acqBatchMerchantBusi.list.PageCallback,
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
        ucf.ajax.commonFn(ctx + "/acqBatchMerchantBusi/list", $("#frmQry").serialize(), function (result) {
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
                        $("<td>").html(obj.applyNo).appendTo(_tr);
                        $("<td>").html(obj.memo).appendTo(_tr);
                        $("<td>").html(obj.time).appendTo(_tr);
                        $("<td>").html(obj.statusLable).appendTo(_tr);
                        $("<td>").html(obj.appOpno).appendTo(_tr);
                        $("<td>").html(obj.appTime).appendTo(_tr);
                        var _operTd = $("<td>");

                        var _auditBtMsg = "";
                        if (obj.status == 0) {
                            _auditBtMsg = "&nbsp;&nbsp;审核";
                        } else if (obj.status == 2) {
                            _auditBtMsg = "&nbsp;&nbsp;查看审核意见";
                        }
                        var _audit = $("<a>").attr({
                            "href": ctx + "/acqBatchMerchantBusi/" + obj.id + ucf.common.getUrlRandom(),
                            "data-toggle": "modal",
                            "data-target": "#myModal"
                        }).html(_auditBtMsg);

                        var _down = $("<a>").attr({"href": "javascript:void(0)"}).html("下载文件").on("click", function () {
                            ucf.common.download(ctx + "/acqBatchMerchantBusi/downFile", {"id": obj.id}, "get")
                        });
                        _down.appendTo(_operTd);
                        _audit.appendTo(_operTd);
                        _operTd.appendTo(_tr);
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
        acqBatchMerchantBusi.list.getRecords();
    }


};
acqBatchMerchantBusi.info = {
    init: function () {
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity, width: '100%'});



        $("#frmEdit").validate({
            debug: true,
            rules: {
                opinions: {
                    required: function () {
                        return $("#status").val() == '2';
                    }
                },
                status: {
                    required: true
                }
            },
            messages: {},
            submitHandler: function () {
                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                    if (result.status == 'OK') {
                        acqBatchMerchantBusi.list.getRecords();
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