if (typeof whiteList1 == 'undefined') {
    whiteList1 = {};
}
var totalElements = 0;
whiteList1.list = {
    init: function () {
        $("select").select2({width: '100%'});
        $(".dateSelector").click(function () {
            WdatePicker();
        });
        whiteList1.list.dateTagDeal('dateTagList', 'beginDate', 'endDate');
        whiteList1.list.qrywhiteList1Data();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            whiteList1.list.qrywhiteList1Data();
        });
        $("#frmQry").on("submit", function () {
            whiteList1.list.qryPageData();
            return false;
        });
        $("#btnExport").on("click", function () {
            ucf.common.download(ctx + "/whiteList1/export", $("#frmQry").serialize());
        });
    },
    qrywhiteList1Data: function () {
        whiteList1.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: whiteList1.list.PageCallback,
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
        ucf.ajax.commonFn("/app/whiteList1/list", $("#frmQry").serialize(), function (result) {

            if (result.status == 'OK') {
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html = "", defHtml = '<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content, function (index, obj) {
                        html += '<tr id="whiteList1Info_' + obj.id + '">'
                            + '<td><a href="whiteList1/' + obj.id + ucf.common.getUrlRandom() + '" data-toggle="modal" data-target="#myModal">' + obj.id + '</a></td>'
                            + '<td>' + obj.packetName + '</td>'
                            + '<td>' + obj.descript + '</td>'
                            + '<td>' + obj.uid + '</td>'
                            + '<td>' + obj.createTime + '</td>'
                            + '<td>' + obj.updateTime + '</td>'
                            + '<td><a href="/app/whiteList1/delete/' + obj.id + '" class="btn btn-danger btn-sm" data-toggle="tooltip"  title="确定删除？">删除</a></td>'
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
        whiteList1.list.getRecords();
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


whiteList1.info = {
    init: function () {
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("select[name='type']").on("change", function () {
            if (typeof $("#divwhiteList1Acc") != 'undefined') {
                $("#divwhiteList1Acc").toggle();
            }
            if (typeof $("#divRole") != 'undefined') {
                $("#divRole").toggle();
            }
        });


        $("#frmEdit").validate({
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
                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                    if (result.status == 'OK') {
                        whiteList1.list.qrywhiteList1Data();
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