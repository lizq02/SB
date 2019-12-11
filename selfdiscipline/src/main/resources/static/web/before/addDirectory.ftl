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

    <link type="text/css" rel="stylesheet" href="/static/css/after/userFileManager.css"/>
</head>
<body>
<div>
    <form id="form1" class="form-horizontal" style="padding: 10px 15px;">
        <div class="form-group">
            <label class="col-sm-2 control-label">父目录</label>
            <div class="col-sm-10">
                <#if pid == "0">
                    <p id="pid" pid="${pid!}" class="form-control-static">无</p>
                <#else>
                    <p id="pid" pid="${pid!}" class="form-control-static">${pname!}</p>
                </#if>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label"><em style="color: red;">*</em>目录名称</label>
            <div id="catalogueNameDiv" class="col-sm-10">
                <input type="text" id="catalogueName" class="form-control" placeholder="目录名称" maxlength="20"/>
                <span id="cataNameHelpBlock" class="help-block hidden" style="color: red;font-size: 8px;">提示:</span>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">排序号</label>
            <div id="orderNoDiv" class="col-sm-10">
                <input type="number" id="orderNo" class="form-control" min="1" value="1" maxlength="100"/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">描述</label>
            <div id="describeDiv" class="col-sm-10">
                <textarea id="cataDescribe" class="form-control" rows="4" placeholder="目录描述" maxlength="200"></textarea>
                <span id="describeHelpBlock" class="help-block pull-right">当前已输入<span id="nowLength">0</span>字符,最大输入200字符</span>
            </div>
        </div>
        <div class="form-group" style="text-align: center;">
            <button type="button" class="btn btn-link" onclick="fnSave()">保存</button>
            <button type="button" class="btn btn-link" onclick="fnClose()">关闭</button>
        </div>
    </form>
</div>
</body>
<script>
    $(document).ready(function () {
    });

    // 保存
    function fnSave() {
        var params = {
            "cataloguePid" : $("#pid").attr("pid")
        };
        Base.submit("form1", "userFileManagerController!addDirectory.do", params, function () {
            // 提交验证
            if ($("#catalogueName").val().length < 1){
                $("#catalogueNameDiv").addClass("has-error");
                $("#catalogueName").focus();
                return false;
            }else {
                $("#catalogueNameDiv").removeClass("has-error");
                $("#cataNameHelpBlock").removeClass("show");
                $("#cataNameHelpBlock").addClass("hidden");
            }
        }, function (data) {
            if (data.code == "0"){
                parent.Base.closeWindow("addDirectory");
            }else {
                alert(data.message);
            }
        });
    }

    // 关闭
    function fnClose() {
        parent.Base.closeWindow("addDirectory");
    }

    window.onload = function () {
        //获取文本内容和长度函数
        function txtCount(el) {
            var val = el.value;
            var eLen = val.length;
            return eLen;
        }
        var el = document.getElementById('cataDescribe');
        var node = document.getElementById('nowLength');
        el.addEventListener('input', function () {
            node.innerHTML = txtCount(this);
        });

        el.addEventListener('propertychange', function () {//兼容IE
            node.innerHTML = txtCount(this);
        });
    }
</script>
</html>
