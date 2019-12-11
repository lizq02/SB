<!DOCTYPE html>
<html lang="en" style="height: 100%;">
<head>
    <meta content="text/html" charset="UTF-8">
    <title>自律</title>
    <#--引入页面公共的js、css等-->
    <script type="text/javascript" src="/static/ta/js/jquery-3.4.1.js"></script>

    <#--引入bootstrap配置-->
    <script type="text/javascript" src="/static/ta/js/bootstrap.min.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/ta/css/bootstrap.min.css"/>

    <#--引入系统js/css-->
    <script type="text/javascript" src="/static/ta/js/common.js"></script>
    <link type="text/css" rel="stylesheet" href="/static/ta/css/common.css"/>

    <script type="text/javascript" src="/static/ta/js/md5.js"></script>

    <link type="text/css" rel="stylesheet" href="/static/css/before/login.css"/>
</head>
<body>
<form id="loginForm" class="form-horizontal">
    <div class="form-group">
        <label class="col-sm-3 control-label">username</label>
        <div id="usernameDiv" class="col-sm-9">
            <input type="text" id="username" class="form-control" placeholder="请输入用户名"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">Password</label>
        <div id="passwordDiv" class="col-sm-9">
            <input type="password" id="password" class="form-control" placeholder="密码"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">验证码</label>
        <div id="authCodeDiv" class="col-sm-5">
            <input type="text" id="authCode" class="form-control"/>
        </div>
        <div class="col-sm-4">
            <div id="checkCode" title="看不清,换一张" class="form-control" onclick="createCode(4)">
                <span id="checkCode_0">O</span>
                <span id="checkCode_1">O</span>
                <span id="checkCode_2">O</span>
                <span id="checkCode_3">O</span>
            </div>
        </div>
    </div>
    <div id="helpBlockDiv" class="form-group">
        <label class="col-sm-3 control-label"></label>
        <div class="col-sm-9">
            <span id="helpBlock" class="help-block" style="color: red;font-size: 8px;">${errormsg!}</span>
        </div>
    </div>
    <div class="form-group" style="text-align: center;">
        <button type="button" class="btn btn-link" onclick="fnLogin()">登录</button>
        <button type="button" class="btn btn-link" onclick="fnRegister()">注册</button>
    </div>
</form>
</body>
<script>
    $(document).ready(function () {
    })

    // 登录
    function fnLogin() {
        var helpBlockDiv = $("#helpBlockDiv");
        var helpBlock = $("#helpBlock");
        var username = $("#username").val();
        var pwd = $("#password").val();
        if (!username) {
            helpBlockDiv.addClass("show");
            helpBlockDiv.removeClass("hidden");
            helpBlock.html("提示:用户名不能为空");
            $("#username").focus();
            return false;
        }
        if (!pwd) {
            helpBlockDiv.addClass("show");
            helpBlockDiv.removeClass("hidden");
            helpBlock.html("提示:密码不能为空");
            $("#password").focus();
            return false;
        }
        var checkCode = $("#checkCode").attr("checkCode");
        var checkCode_ = $("#authCode").val() + "";
        /*if (checkCode.toUpperCase() != checkCode_.toUpperCase()){
            helpBlockDiv.addClass("show");
            helpBlockDiv.removeClass("hidden");
            helpBlock.html("提示:验证码错误");
            createCode(4);
            return false;
        }*/
        var sessionId = "${sessionId}";
        var i = hex_md5(hex_md5(hex_md5(pwd) + username) + sessionId);
        location.href = "loginController!login.do?username=" + username + "&i=" + i;
    }

    // 注册
    function fnRegister() {

    }

    // 页面加载时，生成随机验证码
    window.onload = function () {
        createCode(4);
    };

    // 所有候选组成验证码的字符，当然也可以用中文的
    var CODECHARS = new Array(1, 2, 3, 4, 5, 6, 7, 8, 9,
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z');

    //生成验证码的方法
    function createCode(length) {
        var codeStr = "";
        var codeLength = parseInt(length); //验证码的长度
        //循环组成验证码的字符串
        for (var i = 0; i < codeLength; i++) {
            //获取随机验证码下标
            var code = CODECHARS[Math.floor(Math.random() * CODECHARS.length)];
            codeStr += code;
            $("#checkCode_" + i).html(code);
        }
        $("#checkCode").attr("checkCode", codeStr);
    }
</script>
</html>
