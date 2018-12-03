if (typeof sysLog == 'undefined') {
    sysLog = {};
}
var totalElements=0;

sysLog.list={
    init:function(){
        $(".dateSelector").click(function(){WdatePicker();});
        sysLog.list.dateTagDeal('dateTagList', 'beginDate', 'endDate');
        $("#beginDate").val(ucf.common.getDate("","yyyy-MM-dd"));
        $("#endDate").val(ucf.common.getDate("","yyyy-MM-dd"));
        sysLog.list.qryPageData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            sysLog.list.qryPageData();
        });
        $("#frmQry").on("submit",function(){
            sysLog.list.qryPageData();
            return false;
        });
    },
    qryPageData:function(){
        sysLog.list.getRecords();
        $("#Pagination").empty();
        if(totalElements>0){
            $("#Pagination").pagination(totalElements, {
                callback: sysLog.list.PageCallback,
                prev_text: '上一页',
                next_text: '下一页',
                items_per_page: $("input[name='size']").val(),
                num_display_entries: 5,
                current_page: $("input[name='page']").val(),
                load_first_page:false
            });
        }

    },

    getRecords:function(){
        ucf.ajax.commonFn(ctx+"/sys/log",$('#frmQry').serialize(),function(result){

            if(result.status =='OK'){
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html="",defHtml='<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content,function(index,obj){
                        html += '<tr id="sysLog_'+obj.id+'">'
                        +'<td>'+obj.userAccount +'</td>'
                        +'<td>'+obj.time +'</td>'
                        +'<td>'+obj.title+'</td>'
                        +'<td>'+obj.log+'</td>'
                        +'<td>'+obj.ip+'</td>'+'</tr>';
                    });
                $("#tblData tbody").append(html==""?defHtml:html);
            }else{
                $.pnotify({title: result.message,type: 'error'});
            }
        });
    },

    PageCallback:function(pageIndex,jq) {
        $("input[name='page']").val(pageIndex);
        sysLog.list.getRecords();
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
