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

    <style type="text/css">
        .border-style {
            border: 1px solid #b0b0c4 !important;
            border-radius: 0px !important;
        }

        .checkCode {
            display: inline-block;
            width: calc(50% - 20px);
            height: 34px;
            margin-left: 35px;
            padding-top: 8px;
            border: 1px solid deepskyblue;
            cursor: pointer;
            border-radius: 0px !important;
            color: deepskyblue;
        }

        .checkCode:hover {
            background-color: deepskyblue;
            color: white;
        }

        #submit {
            cursor: pointer;
            background-color: deepskyblue;
            color: white;
            border-radius: 0px !important;
        }

        #submit:hover {
            background-color: #00a5e1;
        }

        .agreement {
            margin-left: 5px;
            margin-right: 5px;
            color: deepskyblue;
        }

        body {
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
    </style>
</head>
<body style="height: 100%;overflow-x: none;width: 98%;margin-left: 1%;">
<div class="row" style="background-color: #f5f6f7;min-height: 100%">
    <div class="col-md-2"></div>
    <div class="col-md-8">
        <h2 class="text-center">系统用户注册</h2>
        <div style="background-color: white;">
            <div class="row">
                <div class="col-md-3"></div>
                <div class="col-md-6">
                    <form id="form1" style="padding: 20px 10px;" autocomplete="off">
                        <div class="form-group">
                            <input type="text" class="form-control border-style" id="loginid" placeholder="登录账号"
                                   maxlength="11"/>
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control border-style" id="username" placeholder="用户名称"
                                   maxlength="16">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control border-style" id="password" placeholder="密码"
                                   maxlength="16" minlength="6">
                        </div>
                        <div class="form-group">
                            <input type="password" class="form-control border-style" id="password_" placeholder="确认密码"
                                   maxlength="16" minlength="6">
                        </div>
                        <div class="text-center" style="margin-bottom: 10px;">
                            <span style="background-color: white; ">&nbsp;&nbsp;&nbsp;手机信息&nbsp;&nbsp;&nbsp;</span>
                            <div style="float: left; width: 100%;z-index: -1; border-top: 1px solid lavender;margin-top: -12px;"></div>
                        </div>
                        <div class="form-group">
                            <input type="text" onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"
                                   class="form-control border-style"
                                   id="phonenumber" placeholder="手机号码" maxlength="11" minlength="11">
                        </div>
                        <div class="form-group">
                            <input type="text" class="form-control border-style"
                                   style="display: inline; width: calc(50% - 20px);" id="checkCode" placeholder="验证码">
                            <div class="form-control text-center checkCode" onclick="fnGetCheckcode()">
                                获取验证码
                            </div>
                        </div>
                        <div class="checkbox">
                            <label>
                                <input type="checkbox">我已阅读并同意<a class="agreement">系统协议</a>
                            </label>
                        </div>
                        <div class="form-group">
                            <div class="text-center form-control" id="submit" onclick="fnSubmit()">
                                同意协议并提交
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="text-center" style="margin-top: 15px;">
            <a style="cursor: pointer" href="/loginController.do">
                <
                <登录已有账号
            </a>
        </div>
    </div>
</div>
</body>
<script>
    $(document).ready(function () {
    });

    // 提交
    function fnSubmit() {
        var loginid = $("#loginid").val();
        var username = $("#username").val();
        var password = $("#password").val();
        var password_ = $("#password_").val();
        var phonenumber = $("#phonenumber").val();
        var checkCode = $("#checkCode").val();
        var params = {
            "loginid": loginid,
            "username": username,
            "password": hex_md5(password),
            "password_": hex_md5(password_),
            "phonenumber": phonenumber,
            "checkCode": checkCode
        };
        Base.submit("", "loginController!register.do", params, function () {
            // 提交验证
            if ($("#loginid").val().length < 1) {
                $("#loginid").addClass("has-error");
                $("#loginid").focus();
                return false;
            }
            if ($("#username").val().length < 1) {
                $("#username").addClass("has-error");
                $("#username").focus();
                return false;
            }
            if ($("#password").val().length < 1) {
                $("#password").addClass("has-error");
                $("#password").focus();
                return false;
            }
            if ($("#password_").val().length < 1) {
                $("#password_").addClass("has-error");
                $("#password_").focus();
                return false;
            }
            if ($("#phonenumber").val().length < 1) {
                $("#phonenumber").addClass("has-error");
                $("#phonenumber").focus();
                return false;
            }
            if ($("#checkCode").val().length < 1) {
                $("#checkCode").addClass("has-error");
                $("#checkCode").focus();
                return false;
            }
        }, function (data) {
            if (data.code == "0") {
                alert("注册成功");
                location.href = "loginController.do";
            } else {
                alert(data.message);
            }
        });
    }

    // 获取验证码
    function fnGetCheckcode() {

    }
</script>
</html>
