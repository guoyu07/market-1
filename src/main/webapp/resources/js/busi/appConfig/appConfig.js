if (typeof appconfig == 'undefined') {
    appconfig = {};
}
var totalElements = 0;
appconfig.list = {
    init: function () {
        $("select").select2({width: '100%'});
        $(".dateSelector").click(function () {
            WdatePicker();
        });
        appconfig.list.dateTagDeal('dateTagList', 'beginDate', 'endDate');
        appconfig.list.qryappconfigData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            appconfig.list.qryappconfigData();
        });
        $("#frmQry").on("submit", function () {
            appconfig.list.qryPageData();
            return false;
        });
        $("#btnExport").on("click", function () {
            ucf.common.download(ctx + "/appconfig/export", $("#frmQry").serialize());
        });
    },
    qryappconfigData: function () {
        appconfig.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#terminalCount2")[0].innerText="共"+totalElements +"条记录，每页50条";
            $("#checkedAll").prop("checked",false);
            $("#Pagination").pagination(totalElements, {
                callback: appconfig.list.PageCallback,
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
        ucf.ajax.commonFn("/app/config/list", $("#frmQry").serialize(), function (result) {

            if (result.status == 'OK') {
                $("#checkedAll").prop("checked",false);
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html = "", defHtml = '<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content, function (index, obj) {
                        html += '<tr data-imeiid="' + obj.imei + '">'
                            + '<td><a href="/app/config/show/' + obj.imei + ucf.common.getUrlRandom() + '" data-toggle="modal" data-target="#myModal">' + obj.imei + '</a></td>'
                            + '<td>' + obj.sn + '</td>'
                            + '<td>' + obj.userIdName + '</td>'
                            + '<td>' + obj.shopName + '</td>'
                            + '<td><input type="checkbox" name="imeiCheck" onclick="imeiCheck($(this))" value="' + obj.imei + '" id=""></td>'
                            + '</tr>';
                    });
                $("#tblData tbody").append(html == "" ? defHtml : html);
            } else {
                $.pnotify({title: result.message, type: 'error'});
            }
        });
    },

    PageCallback: function (pageIndex, jq) {
        $("input[name='page']").val(pageIndex);
        appconfig.list.getRecords();
    },
    dateTagDeal: function (className, beginDateId, endDateId) {
        var _dateTags = ['yesterday', 'pastSevenToday', 'pastOneMonth', 'pastThreeMonth', 'pastYear'];
        var _dateList = $("." + className).children();
        _dateList.on("click", function () {
            _dateList.removeClass("greenBtn").removeClass("scrCur");
            $(this).addClass("greenBtn").addClass("scrCur");
            var _dateTagIndex = $(this).attr("dateTagIndex");
            var _dateTagName = _dateTags[_dateTagIndex];
            var _beginDate = ucf.common.getDate(_dateTagIndex, 'yyyy-MM-dd');
            var _endDate = ucf.common.getDate("0", 'yyyy-MM-dd');
            $("#" + beginDateId).val(_beginDate);
            $("#" + endDateId).val(_endDate);
            $("#btnQry").click();
        });

        $("#" + beginDateId).on("click", function () {
            _dateList.removeClass("greenBtn").remove("scrCur");
        });

        $("#" + endDateId).on("click", function () {
            _dateList.removeClass("greenBtn").remove("scrCur");
        });
    }
};


appconfig.info = {
    init: function () {
        $("#frmappconfig select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("select[name='type']").on("change", function () {
            if (typeof $("#divappconfigAcc") != 'undefined') {
                $("#divappconfigAcc").toggle();
            }
            if (typeof $("#divRole") != 'undefined') {
                $("#divRole").toggle();
            }
        });


        $("#frmappconfig").validate({
            rules: {
                name: {
                    required: true,
                    rangelength: [6, 20],
                    isRightfulString: true
                },
                password: {
                    required: updateType == 'create',
                    rangelength: [6, 20]
                },
                roleIds: {
                    required: function () {
                        return $("select[name=type]").val() == '1';
                    }
                },
                name: {required: true}
            },
            ignore: 'input[type=hidden]',
            errorPlacement: function (error, element) {
                if (element.is(":checkbox")) {
                    error.appendTo(element.parent());
                } else if (element.is(":text") || element.is(":password")) {
                    element.after(error);
                } else {
                    error.appendTo(element.next());
                }
            },
            submitHandler: function (form) {
                ucf.ajax.commonFn($('#frmappconfig').attr('action'), $('#frmappconfig').serialize(), function (result) {
                    if (result.status == 'OK') {
                        appconfig.list.qryappconfigData();
                        $('[data-dismiss]').click();
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
}