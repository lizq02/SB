<!DOCTYPE html>
<html lang="en">
<head>
    <META http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>用户文件管理</title>
    <#--<link rel="import" href="/static/ta/include.ftl"/>-->
    <#--引入页面公共的js、css等-->
    <script type="text/javascript" src="/static/ta/js/jquery-3.4.1.js"></script>

    <#--引入bootstrap配置-->
    <script type="text/javascript" src="/static/ta/js/bootstrap.min.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/ta/css/bootstrap.min.css"/>

    <#--引入系统js/css-->
    <script type="text/javascript" src="/static/ta/js/common.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/ta/css/common.css"/>
    
    <style type="text/css">
        .prompt {
            font-size: 12px;
            color: #ff7064;
        }
    </style>
</head>
<body>
<form id="form1" class="form-horizontal" style="padding: 10px 15px;">
    <div class="form-group">
        <div id="catalogueNameDiv" class="col-sm-12">
            <div class="input-group">
                <span class="input-group-addon">目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;录</span>
                <div id="catalogue"></div>
            </div>
        </div>
    </div>
    <div class="form-group">
        <div id="catalogueNameDiv" class="col-sm-12">
            <div class="input-group">
                <span class="input-group-addon">上传文件</span>
                <input type="file" id="file">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-12 prompt">
            提示:<br />
            1.上传文件最大支持5MB.<br />
            2.只能上传[doc, docx, xls, xlsx, ppt, pptx, html, htm, pdf]类型的文件.<br />
            3.当上传文件不为pdf、html和htm格式时，系统需进行文件转换，可能需要一定时间，敬请谅解.
        </div>
    </div>
    <div class="form-group" style="text-align: center;">
        <button type="button" class="btn btn-link" onclick="fnSave()">保存</button>
        <button type="button" class="btn btn-link" onclick="fnClose()">关闭</button>
    </div>
</form>
</body>
<script>
    $(document).ready(function () {
        Base.submit("", "userFileManagerController!queryUserCatalogues.do", null, null, function (data) {
            if (data.code == "0") {
                var list = data.data;
                Base.selectInit({
                    id: "catalogue",
                    isOpen: true,
                    rootValue: "0",
                    keyId: "id",
                    keyValue: "catalogueName",
                    keyPid: "cataloguePid",
                    data: list
                })
            }
        });
    });

    // 保存
    function fnSave() {
        var catalogue = Base.getSelectData("catalogue");
        var catalogueId = catalogue.nodeId;
        if (catalogueId == undefined || catalogueId == null || catalogueId.length < 1) {
            alert("请选择目录");
            return;
        }
        var files = document.getElementById("file").files;
        if (files.length < 1) {
            alert("请选择要上传的文件")
            return;
        }
        var file = files[0];
        var size = file.size / 1024 / 1024;
        if (size > 5.00) {
            alert("最大只能上传5MB的文件")
            return
        }
        var formData = new FormData();
        formData.append("catalogueId", catalogueId);
        // 将文件添加到formData对象中
        formData.append("file", file);
        var maskid = Base.showMask();
        // ajax 异步上传
        $.ajax({
            type: "post",
            url: "userFileManagerController!uploadFile.do",// 上传请求url
            data: formData,// 上传参数
            processData: false,// 必须，设置不转换为string，默认为true
            contentType: false,
            success: function (data) {
                if (data.code == "0"){
                    fnClose();
                }else {
                    alert(data.message);
                }
                Base.hideMask(maskid);
            },
            error: function (e) {
                alert("上传失败");
                Base.hideMask(maskid);
            }
        });
    }

    // 关闭
    function fnClose() {
        parent.Base.closeWindow("insertFile");
    }
</script>
</html>
