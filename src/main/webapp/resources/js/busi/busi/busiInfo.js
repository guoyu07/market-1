if (typeof busi == 'undefined') {
    busi = {};
}
var totalElements=0;
busi.list={
    init:function(){
        $("select").select2({minimumResultsForSearch: Infinity,width:'100%'});
        busi.list.qryPageData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            busi.list.qryPageData();
        });
        $("#frmQry").on("submit",function(){
            busi.list.qryPageData();
            return false;
        });
    },
    qryPageData:function(){
        busi.list.getRecords();
        $("#Pagination").empty();
        if(totalElements>0){
            $("#Pagination").pagination(totalElements, {
                callback: busi.list.PageCallback,
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
        ucf.ajax.commonFn(ctx+"/busiInfo/list",$("#frmQry").serialize(),function(result){

            if(result.status =='OK'){
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html="",defHtml='<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content,function(index,obj){
                        html += '<tr id="busi_'+obj.busiCode+'">'
                        +'<td><a href="'+ctx +'/busiInfo/'+obj.busiCode +ucf.common.getUrlRandom() +'" data-toggle="modal" data-target="#myModal">'+obj.busiCode+'</a></td>'
                        +'<td>'+obj.busiName+'</td>'
                        +'<td>'+obj.busiEnName+'</td>'
                        +'<td>'+obj.parentBusiCode+'</td>'
                        +'<td>'+obj.busiDesc+'</td>'
                        +'<td>'+obj.statusLable+'</td>'
                        +'<td>'+obj.appTime+'<br/>'+obj.appOpno+'</td>'
                        +'</tr>';
                    });
                $("#tblData tbody").append(html==""?defHtml:html);
            }else{
                $.pnotify({title: result.message,type: 'error'});
            }
        });
    },

    PageCallback:function(pageIndex,jq) {
        $("input[name='page']").val(pageIndex);
        busi.list.getRecords();
    }

};
busi.info={
    init:function(){
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity,width:'100%'});
        $("#frmEdit").validate({
            debug:true,
            rules: {
                busiCode:{
                    required: true,
                    rangelength: [1, 10]
                },
                busiName:{
                    required: true,
                    rangelength: [2, 20]
                },
                busiEnName:{
                    required: true,
                    rangelength: [2, 20]
                },
                rateValue:{
                    required: true,isDecimal:true
                },
                minAmount:{
                    isDecimal:true
                },
                maxAmount:{
                    isDecimal:true,
                    twoNumVerify: function () {
                        return {"num": $("input[name='minAmount']").val(), "type": "L"};
                    }
                },
                biQuota:{
                    isDecimal:true
                },
                dayQuota:{
                    isDecimal:true
                },
                monthQuota:{
                    isDecimal:true
                },
                yearQuota:{
                    isDecimal:true
                }
            },
            messages: {
                maxAmount: {
                    "twoNumVerify": "封顶金额必须大于保底金额"
                }
            },
            submitHandler:function(){

                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(),function(result){
                    if(result.status =='OK'){
                        busi.list.getRecords();
                        $('[data-dismiss]').click();
                        $.pnotify({title: result.message,type: result.status});
                    }else{
                        $("#saveBtn").attr("data-content",result.message);
                        $("#saveBtn").popover('toggle');
                        setTimeout(function(){$("#saveBtn").popover('toggle');}, 2000);
                    }
                });
            }
        });
    }
};