if (typeof app == 'undefined') {
    app = {};
}
var totalElements = 0;
app.list = {
    init: function () {
        $("select").select2({width: '100%'});
        $(".dateSelector").click(function () {
            WdatePicker();
        });
        app.list.dateTagDeal('dateTagList', 'beginDate', 'endDate');
        app.list.qryappData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            app.list.qryappData();
        });
        $("#frmQry").on("submit", function () {
            app.list.qryPageData();
            return false;
        });
        $("#btnExport").on("click", function () {
            ucf.common.download(ctx + "/app/export", $("#frmQry").serialize());
        });
    },
    qryappData: function () {
        app.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: app.list.PageCallback,
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
        ucf.ajax.commonFn("app/list", $("#frmQry").serialize(), function (result) {

            if (result.status == 'OK') {
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html = "", defHtml = '<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content, function (index, obj) {
                        html += '<tr id="appInfo_' + obj.id + '">'
                            + '<td><a href="app/' + obj.id + ucf.common.getUrlRandom() + '" data-toggle="modal" data-target="#myModal">' + obj.id + '</a></td>'
                            + '<td>' + obj.appName + '</td>'
                            + '<td>' + obj.appVersionName + '</td>'
                            + '<td>' + (obj.fileSize/1024/1024).toFixed(2) + 'M</td>'
                            + '<td><img style="width: 50px;height: 50px" src="' +obj.iconLocation + '"></td>'
                            + '<td>' + obj.appType + '</td>'
                            + '<td>' + obj.downCount + '</td>'
                            + '<td>' + obj.uid + '</td>'
                            + '<td><a href="app/delete/' + obj.id + '" class="btn btn-danger btn-sm" data-toggle="tooltip"  title="确定删除？">删除</a></td>'
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
        app.list.getRecords();
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

app.import = {
    init: function () {
        $("#uploadFile").uploadifive({
            auto: true,
            multi: false,
            queueID: 'fileQueue',
            fileType: 'application/vnd.ms-excel,text/plain',
            buttonText: "选择文件",
            formData: "",
            'uploadScript': ctx + '/fileUpload/appFile',
            'onUploadComplete': function (file, data) {
                var json = eval('(' + data + ')');
                if (json.status == 'OK') {
                    $("#appFile").val(json.message);
                } else {
                    $.pnotify({title: json.message, type: json.status});
                }
            }
        });


        $("#frmEdit").validate({
      /*      rules: {
                appFile: {
                    required: true
                }
            },
            messages: {
                "appFile": {
                    "required": "请上传用户文件"
                }
            },
            ignore: '',*/
            /*errorPlacement: function (error, element) {
                console.log(error);
                var index = error[0].innerText.indexOf('Please');
                if(index==0){

                }
                else {
                    $("#uploadErrorMsg").html(error);
                }


            },*/
            submitHandler: function (form) {
                if( $("#appFile").val() == null || $("#appFile").val() == ""){
                    $("#uploadErrorMsg").html("请上传用户文件");
                }
                else {
                    ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                        if (result.status == 'OK') {
                            app.list.getRecords();
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

app.info = {
    init: function () {
        $("#frmapp select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("select[name='type']").on("change", function () {
            if (typeof $("#divappAcc") != 'undefined') {
                $("#divappAcc").toggle();
            }
            if (typeof $("#divRole") != 'undefined') {
                $("#divRole").toggle();
            }
        });


        $("#frmapp").validate({
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
                ucf.ajax.commonFn($('#frmapp').attr('action'), $('#frmapp').serialize(), function (result) {
                    if (result.status == 'OK') {
                        app.list.qryappData();
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