if (typeof terminal == 'undefined') {
    terminal = {};
}
var totalElements = 0;
terminal.list = {
    init: function () {
        $("select").select2({width: '100%'});
        $(".dateSelector").click(function () {
            WdatePicker();
        });
        terminal.list.dateTagDeal('dateTagList', 'beginDate', 'endDate');
        terminal.list.qryterminalData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            terminal.list.qryterminalData();
        });
        $("#frmQry").on("submit", function () {
            terminal.list.qryPageData();
            return false;
        });
        $("#btnExport").on("click", function () {
            ucf.common.download(ctx + "/terminal/export", $("#frmQry").serialize());
        });
    },
    qryterminalData: function () {
        terminal.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#terminalCount")[0].innerText="共"+totalElements +"条记录，每页50条";
            $("#Pagination").pagination(totalElements, {
                callback: terminal.list.PageCallback,
                prev_text: '上一页',
                next_text: '下一页',
                items_per_page: $("input[name='size']").val(),
                num_display_entries: 5,
                current_page: $("input[name='page']").val(),
                load_first_page: false,
                totalData:totalElements,
                showData:$("input[name='size']").val()
            });
        }

    },

    getRecords: function () {
        ucf.ajax.commonFn("/terminal/list", $("#frmQry").serialize(), function (result) {

            if (result.status == 'OK') {
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html = "", defHtml = '<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined") {
                    $.each(result.message.content, function (index, obj) {
                        html += '<tr id="terminalInfo_' + obj.imei + '">'
                            + '<td><a href="terminal/' + obj.imei + ucf.common.getUrlRandom() + '" data-toggle="modal" data-target="#myModal">' + obj.imei + '</a></td>'
                            + '<td>' + obj.sn + '</td>'
                            + '<td>' + obj.userIdName + '</td>'
                            + '<td>' + obj.shopName + '</td>'
                            + '<td>' + obj.shopContacts + '</td>'
                            + '<td>' + obj.shopPhone + '</td>'
                            + '<td>' + obj.area + '</td>'
                            + '<td>' + obj.createTime + '</td>'
                            + '<td>' + obj.updateTime + '</td>'
                            + '<td><a href="terminal/delete/' + obj.imei + '" class="btn btn-danger btn-sm" data-toggle="tooltip"  title="确定删除？">删除</a></td>'
                            + '</tr>';
                    });
                    $("#totalCount").text(result.message.totalElements);
                }else
                    $("#totalCount").text("");
                $("#tblData tbody").append(html == "" ? defHtml : html);
            } else {
                $.pnotify({title: result.message, type: 'error'});
            }
        });
    },

    PageCallback: function (pageIndex, jq) {
        $("input[name='page']").val(pageIndex);
        terminal.list.getRecords();
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

terminal.import = {
    init: function () {
        $("#uploadFile").uploadifive({
            auto: true,
            multi: false,
            queueID: 'fileQueue',
            fileType: 'application/vnd.ms-excel,text/plain',
            buttonText: "选择文件",
            formData: "",
            'uploadScript': ctx + '/fileUpload/terminalFile',
            'onUploadComplete': function (file, data) {
                var json = eval('(' + data + ')');
                if (json.status == 'OK') {
                    $("#terminalFile").val(json.message);
                } else {
                    $.pnotify({title: json.message, type: json.status});
                }
            }
        });


        $("#frmEdit").validate({
      /*      rules: {
                terminalFile: {
                    required: true
                }
            },
            messages: {
                "terminalFile": {
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
                if( $("#terminalFile").val() == null || $("#terminalFile").val() == ""){
                    $("#uploadErrorMsg").html("请上传用户文件");
                }
                else {
                    ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(), function (result) {
                        if (result.status == 'OK') {
                            terminal.list.getRecords();
                            $('[data-dismiss]').click();
                            $.pnotify({title: result.message, type: result.status});
                        } else {
                            $("#saveBtn").attr("data-content", result.message);
                            $("#saveBtn").popover('toggle');
                            setTimeout(function () {
                                $("#saveBtn").popover('toggle');
                            }, 10000);
                        }
                    });
                }

            }
        });
    }
};

terminal.info = {
    init: function () {
        $("#frmterminal select").select2({minimumResultsForSearch: Infinity, width: '100%'});

        $("select[name='type']").on("change", function () {
            if (typeof $("#divterminalAcc") != 'undefined') {
                $("#divterminalAcc").toggle();
            }
            if (typeof $("#divRole") != 'undefined') {
                $("#divRole").toggle();
            }
        });


        $("#frmterminal").validate({
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
                }
            // ,
            //     sn: {required: true,
            //         rangelength: [16, 16],}
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
                ucf.ajax.commonFn($('#frmterminal').attr('action'), $('#frmterminal').serialize(), function (result) {
                    if (result.status == 'OK') {
                        terminal.list.qryterminalData();
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