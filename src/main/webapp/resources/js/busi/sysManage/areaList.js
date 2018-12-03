if (typeof user == 'undefined') {
    user = {};
}
var totalElements = 0;
user.list = {
    init: function () {
        $("select").select2({width: '100%'});
        $(".dateSelector").click(function () {
            WdatePicker();
        });
        user.list.dateTagDeal('dateTagList', 'beginDate', 'endDate');
        user.list.qryUserData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            user.list.qryUserData();
        });
        $("#frmQry").on("submit", function () {
            user.list.qryPageData();
            return false;
        });
    },
    qryUserData: function () {
        user.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: user.list.PageCallback,
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
        ucf.ajax.commonFn("area/list", $("#frmQry").serialize(), function (result) {

            if (result.status == 'OK') {
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html = "", defHtml = '<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content, function (index, obj) {
                        html += '<tr id="areaInfo_' + obj.id + '">'
                            + '<td>' + obj.name + '</td>'
                            + '<td>' + obj.createTime + '</td>'
                            + '<td>' + obj.remark + '</td>'
                            + '<td><a href="area/del?id='+obj.id+'">删除</a>  <a href="area/toAdd?id='+obj.id+'&name='+ obj.name +'&remark='+obj.remark+'" data-toggle="modal" data-target="#myModal">编辑</a></td>'
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
        user.list.getRecords();
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

user.import = {
    init: function () {
        $("#uploadFile").uploadifive({
            auto: true,
            multi: false,
            queueID: 'fileQueue',
            fileType: 'application/vnd.ms-excel,text/plain',
            buttonText: "选择文件",
            formData: "",
            'uploadScript': ctx + '/fileUpload/userFile',
            'onUploadComplete': function (file, data) {
                var json = eval('(' + data + ')');
                if (json.status == 'OK') {
                    $("#userFile").val(json.message);
                } else {
                    $.pnotify({title: json.message, type: json.status});
                }
            }
        });


        $("#frmEdit").validate({
            submitHandler: function (form) {
                if( $("#userFile").val() == null || $("#userFile").val() == ""){
                    $("#uploadErrorMsg").html("请上传用户文件");
                }
                else {
                    ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                        if (result.status == 'OK') {
                            user.list.getRecords();
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

            }
        });
    }
};

user.info = {
    init: function () {
        $("#frmUser select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("select[name='type']").on("change", function () {
            if (typeof $("#divUserAcc") != 'undefined') {
                $("#divUserAcc").toggle();
            }
            if (typeof $("#divRole") != 'undefined') {
                $("#divRole").toggle();
            }
        });


        $("#frmUser").validate({
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
            }
        });
    }
}