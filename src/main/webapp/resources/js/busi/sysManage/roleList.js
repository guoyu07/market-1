if (typeof role == 'undefined') {
    role = {};
}
var totalElements=0;
role.list={
    init:function(){
        $("select").select2({minimumResultsForSearch: Infinity,width:'100%'});
        role.list.qryRoleData();
        $("#btnQry").on("click", function () {
            $("input[name='page']").val(0);
            role.list.qryRoleData();
        });
        $("#frmQry").on("submit",function(){
            role.list.qryPageData();
            return false;
        });
    },
    qryRoleData:function(){
        role.list.getRecords();
        $("#Pagination").empty();
        if(totalElements>0){
            $("#Pagination").pagination(totalElements, {
                callback: role.list.PageCallback,
                prev_text: '上一页',
                next_text: '下一页',
                items_per_page: $("input[name='size']").val(),
                num_display_entries: 5,
                current_page: $("input[name='page']").val(),
                load_first_page:false
            });
        }

    },

    getRecords:function(pageIndex){
        ucf.ajax.commonFn("role/list",$('#frmQry').serialize(),function(result){

            if(result.status =='OK'){
                totalElements = result.message.totalElements;
                $('#tblData tbody').empty();//移除所有的数据行
                var html="",defHtml='<tr><td colspan="20" style="text-align: center">没有搜索到数据</td></tr>';
                var sysFlag="";
                if (typeof result.message.content != "undefined")
                    $.each(result.message.content,function(index,obj){
                        html += '<tr id="roleInfo_'+obj.id+'">'
                        +'<td><a href="'+ctx+'/role/'+obj.id + ucf.common.getUrlRandom()+ '" data-toggle="modal" data-target="#myModal">'+obj.name+'</a></td>'
                        +'<td>'+obj.memo+'</td>'+'</tr>';
                    });
                $("#tblData tbody").append(html==""?defHtml:html);
            }else{
                $.pnotify({title: result.message,type: 'error'});
            }
        });
    },

    PageCallback:function(pageIndex,jq) {
        $("input[name='page']").val(pageIndex);
        role.list.getRecords();
    }

};
role.info={
    init:function(){
        $("#frmEdit select").select2({minimumResultsForSearch: Infinity,width:'100%'});
        $('#menuTree').jstree({
            core:{
                'data' :menuData
            },
            checkbox : {
                keep_selected_style : false
            },
            plugins: ["checkbox"]
        });

        $("#role-box").slimScroll({
            height: 300,
            alwaysVisible: true
        });
        $("#delBtn").on("click", function () {
            role.info.delRole();
        });

        $("#frmEdit").validate({
            debug:true,
            rules: {
                name:{
                    required: true,
                    rangelength: [3, 20]
                }
            },
            submitHandler:function(form){
                var _permissions = $("#menuTree").jstree("get_checked").toString();
                $("input[name='permissions']").val(_permissions);
                //var _param = {id:$("input[name='id']").val(),sysLevel:$("select[name='sysLevel']").val(),name:$("input[name='name']").val(),memo:$("input[name='memo']").val(),permissions:_permissions,_method:$("input[name='_method']").val()};

                ucf.ajax.commonFn($('#frmEdit').attr('action'), $('#frmEdit').serialize(),function(result){
                    if(result.status =='OK'){
                        role.list.getRecords();
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
    },
    delRole:function(){
        var roleId = $("input[name='id']").val();
        var _param={"_method":"delete"};
        confirm_dialog("操作确认","确定删除吗？删除后数据不可恢复！",function(notice, val){
            if(notice){
                ucf.ajax.commonFn($('#frmEdit').attr('action')+"/"+roleId, _param,function(result){
                    if(result.status =='OK'){
                        $("#roleInfo_"+roleId).remove();
                        $('[data-dismiss]').click();
                    }else{
                        $("#delBtn").attr("data-content",result.message);
                        $("#delBtn").popover('toggle');
                        setTimeout(function(){$("#delBtn").popover('toggle');}, 2000);
                    }
                });
            }
        });
    }
}