<!DOCTYPE html>
<html lang="en" style="height: 100%;">
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
<body style="height: 100%;">
<div class="container-fluid height_full">
    <div class="row height_full" style="padding-left: 10px; padding-right: 10px;">
        <div class='col-xs-3 col-md-3 row-left-div' style='min-height: 100%;'>
            <h1 class='bg-info' id="myDirectoryTitle">
                My directory
                <span class="pull-right circle-border add-image hidden" catalogueId="0" style="margin-top: 10px"
                      onclick="addCatalogues(this)">+</span>
            </h1>
            <div id="left_context">
                <#--<div class='item'>Dapibus ac facilisis in
                </div>
                <div>
                    <div class='item' style='padding-left: 25px'>Dapibus ac facilisis in</div>
                    <div class='item'>Dapibus ac facilisis in</div>
                </div>
                <div class='item'>Dapibus ac facilisis in</div>-->
            </div>
        </div>
        <div class="col-xs-9 col-md-9 row-right-div">
        </div>
    </div>
</div>
<#--<div id='testWindow' class='ta-window-mc'>
    <div class='ta-window-div' style='height: 400px;width: 80%;'>
        <div>
            <div class='pull-left ta-window-title'>
                title
            </div>
            <div class='pull-right ta-window-close'></div>
            <div class='pull-right ta-window-maximize'></div>
        </div>
        <iframe id='mainIframe' name='mainIframe' src='#' width='100%' height='100%'
                frameborder='0' scrolling='auto'></iframe>
    </div>
</div>-->
</body>
<script>
    $(document).ready(function () {
        // 加载用户文件目录
        initUserCatalogues();
        // 加载用户文件列表
        queryFiles();
        // 设置 鼠标移入元素，显示加号按钮效果
        installMouseShowAdd();
    });

    // 设置 鼠标移入元素，显示加号按钮效果
    function installMouseShowAdd() {
        // 获取所有要设置该效果的元素：必须保证每个元素下的子元素包含加号按钮
        $("#myDirectoryTitle,.item").hover(
            function () {
                $(this).children(".add-image").removeClass("hidden");
                $(this).children(".add-image").addClass("show");
            },
            function () {
                $(this).children(".add-image").removeClass("show");
                $(this).children(".add-image").addClass("hidden");
            }
        );
    }

    var USER_CATALOGUES = [];

    // 加载用户文件目录
    function initUserCatalogues() {
        Base.submit("", "userFileManager/userFileManagerController!queryUserCatalogues.do", null, null, function (data) {
            var htmlStr = "";
            if (data.code == "0") {
                var list = data.data;
                if (list != null && list.length > 0) {
                    USER_CATALOGUES = list;
                    var item;
                    for (var index in list) {
                        item = list[index];
                        if (item["cataloguePid"] == "0") {
                            htmlStr += "<div class='item' catalogueId='" + item["id"] + "' onclick='queryFiles(this)'>" + item["catalogueName"] +
                                "<span class='pull-right circle-border add-image hidden' catalogueId='" + item["id"] + "' onclick='addCatalogues(this)'>+</span>" +
                                "</div>";
                            htmlStr += getHtmlByItem(item, list, 1);
                        }
                    }
                }
            }
            $("#left_context").html(htmlStr);
        });
    }

    // 递归函数：平均目录html
    function getHtmlByItem(node, list, level) {
        var html = "";
        if (isParent(node["id"], list)) {
            html += "<div>";
        }
        var id = node["id"];
        var item;
        var paddingLeft = 10 + level * 15;
        for (var index in list) {
            item = list[index];
            if (item["cataloguePid"] == id) {
                html += "<div class='item' style='padding-left: " + paddingLeft + "px' catalogueId='" + item["id"] + "' onclick='queryFiles(this)'>" + item["catalogueName"] +
                    "<span class='pull-right circle-border add-image hidden' catalogueId='" + item["id"] + "' onclick='addCatalogues(this)'>+</span>" +
                    "</div>";
                if (isParent(item["id"], list)) {
                    html += getHtmlByItem(item, list, ++level);
                }
            }
        }
        if (isParent(node["id"], list)) {
            html += "</div>";
        }
        return html;
    }

    // 判断该节点是否是父节点
    function isParent(id, list) {
        var flag = false;
        $.each(list, function (i, n) {
            if (n["cataloguePid"] == id) {
                flag = true;
                return false;
            }
        });
        return flag;
    }

    // 加载用户文件列表: 根据目录id查询该目录及该目录的子目录的所有文件
    function queryFiles(obj) {
        var params = {
            "catalogueId": $(obj).attr("catalogueId")
        };
        Base.submit("", "userFileManager/userFileManagerController!queryFiles.do", params, null, function (data) {
            console.log(data)
        });
    }

    // 点击添加按钮事件
    function addCatalogues(obj, e) {
        // 阻止事件冒泡
        window.event ? window.event.cancelBubble = true : e.stopPropagation();
        var catalogueId = $(obj).attr("catalogueId");
        Base.openWindow("first1", "我的第一个窗口","#", null, 800, "60%", function () {
            alert("close");
        });
    }
</script>
</html>
