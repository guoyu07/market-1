if (typeof terminalSet == 'undefined') {
    terminalSet = {};
}
var totalElements = 0;
terminalSet.list = {
    init: function () {
        terminalSet.list.qryterminalData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            terminalSet.list.qryterminalData();
        });

    },

    qryterminalData: function () {
        terminalSet.list.getRecords();
        $("#Pagination").empty();
        if (totalElements > 0) {
            $("#Pagination").pagination(totalElements, {
                callback: terminalSet.list.PageCallback,
                prev_text: '上一页',
                next_text: '下一页',
                items_per_page: $("input[name='size']").val(),
                num_display_entries: 5,
                current_page: $("input[name='page']").val(),
                load_first_page: false
            });
        }

    },

    PageCallback: function (pageIndex, jq) {
        $("input[name='page']").val(pageIndex);
        terminalSet.list.getRecords();
    },

    getRecords: function () {
        ucf.ajax.commonFn("/terminal/list", $("#frmQry").serialize(), function (result) {

            if (result.status == 'OK') {
                $("#checkedAll").prop("checked",false);
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html = "", defHtml = '<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content, function (index, obj) {
                        html += '<tr data-snid="' + obj.sn + '">'
                            + '<td>' + obj.imei + '</td>'
                            + '<td>' + obj.sn + '</td>'
                            + '<td><input type="checkbox" name="snCheck" onclick="snCheck($(this))" value="' + obj.imei + '"></td>'
                            + '</tr>';
                    });
                $("#tblData tbody").append(html == "" ? defHtml : html);
            } else {
                $.pnotify({title: result.message, type: 'error'});
            }
        });
    }

}