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
        <div id="leftDiv" class='col-xs-3 col-md-3 row-left-div' style='min-height: 100%;'>
            <h1 class='bg-info' id="myDirectoryTitle" onclick="queryFiles()">
                My directory
                <span title='新增' class="pull-right circle-border button-span hidden" catalogueId="0" catalogueName=""
                      style="margin-top: 10px"
                      onclick="addCatalogues(this)">+</span>
            </h1>
            <div id="left_context">
            </div>
        </div>
        <div class="col-xs-9 col-md-9 row-right-div">
            <div style="border-bottom: 1px solid #164fff; padding-bottom: 2px;">
                <span class="orderBy" onclick="fnOrderTime()">
                    <span id="timeDown" class="orderBySpan orderBySpanSelect">↓</span>
                    <span id="timeUp" class="orderBySpan">↑</span>
                    按时间排序
                </span>
                <span class="orderBy" onclick="fnOrderName()">
                    <span id="nameDown" class="orderBySpan">↓</span>
                    <span id="nameUp" class="orderBySpan">↑</span>
                    按名称排序
                </span>
                <button type="button" class="btn btn-default" onclick="fnInsertFile()">新增</button>
            </div>
            <div id="fileDiv" class="row show">
                <div class="col-md-4 col-xs-4" id="fileDiv_1">
                </div>
                <div class="col-md-4 col-xs-4" id="fileDiv_2">
                </div>
                <div class="col-md-4 col-xs-4" id="fileDiv_3">
                </div>
            </div>
            <#--分页-->
            <div id="pagingDiv">
                <div class='my-paging-count' style='margin-left: 20px'>
                    跳转<input type='text' class='form-control'/>页
                </div>
                <div class='my-paging-count my-trailerpage'>尾页</div>
                <div class='my-paging-class'>
                    <li class='my-previous-li'>
                        <span>&laquo;</span>
                    </li>
                    <li>1</li>
                    <li>2</li>
                    <li class='my-next-li'>
                        <span>&raquo;</span>
                    </li>
                </div>
                <div class='my-paging-count my-homepage'>首页</div>
                <div class='my-paging-count'>共12条</div>
            </div>
            <div id="iframeDiv" class="row">
                <div class="col-md-10 col-xs-10">
                    <iframe id="filepreviewWin" src='' width='100%' height='100%' frameborder='0'
                            scrolling='auto'></iframe>
                </div>
                <div class="col-md-2 col-xs-2" id="filepreviewWinRightDiv" style="overflow: auto;">
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script>
    $(document).ready(function () {
        // 加载用户文件目录
        initUserCatalogues();
        // 加载用户文件列表
        queryFiles();
        // 设置 iframeDiv 高度
        document.getElementById("filepreviewWin").height = $("#leftDiv").height() - 60;
        // 设置 右边div 高度
        document.getElementById("filepreviewWinRightDiv").style.height = $("#leftDiv").height() - 60 + "px";

    });

    // 排序方式：timeDown-时间降序 timeUp-时间升序 nameDown-名称降序 nameUp-时间升序
    var ORDER_VALUE = "timeDown";
    // 排序数组
    var ORDER_ARRAY = [];

    // 点击 按时间排序
    function fnOrderTime() {
        var timeDown = $("#timeDown");
        var timeUp = $("#timeUp");
        $("#nameUp").removeClass("orderBySpanSelect");
        $("#nameDown").removeClass("orderBySpanSelect");
        if (timeDown.attr("class").split(" ").length == 2) {
            timeDown.removeClass("orderBySpanSelect");
            timeUp.addClass("orderBySpanSelect");
            ORDER_VALUE = "timeUp";
        } else if (timeUp.attr("class").split(" ").length == 2) {
            timeUp.removeClass("orderBySpanSelect");
            timeDown.addClass("orderBySpanSelect");
            ORDER_VALUE = "timeDown";
        } else {
            timeUp.removeClass("orderBySpanSelect");
            timeDown.addClass("orderBySpanSelect");
            ORDER_VALUE = "timeDown";
        }
        // 排序后重新渲染文件列表
        fnOrderInitFileDiv();
    }

    // 点击 按名称排序
    function fnOrderName() {
        var nameDown = $("#nameDown");
        var nameUp = $("#nameUp");
        $("#timeUp").removeClass("orderBySpanSelect");
        $("#timeDown").removeClass("orderBySpanSelect");
        if (nameDown.attr("class").split(" ").length == 2) {
            nameDown.removeClass("orderBySpanSelect");
            nameUp.addClass("orderBySpanSelect");
            ORDER_VALUE = "nameUp";
        } else if (nameUp.attr("class").split(" ").length == 2) {
            nameUp.removeClass("orderBySpanSelect");
            nameDown.addClass("orderBySpanSelect");
            ORDER_VALUE = "nameDown";
        } else {
            nameUp.removeClass("orderBySpanSelect");
            nameDown.addClass("orderBySpanSelect");
            ORDER_VALUE = "nameDown";
        }
        // 排序后重新渲染文件列表
        fnOrderInitFileDiv();
    }

    // 排序后重新渲染文件列表
    function fnOrderInitFileDiv() {
        var fileDiv_1 = $("#fileDiv_1");
        var fileDiv_2 = $("#fileDiv_2");
        var fileDiv_3 = $("#fileDiv_3");
        var htmlStr_1 = "";
        var htmlStr_2 = "";
        var htmlStr_3 = "";
        // 根据排序方式进行排序
        fnOrder();
        for (var index in ORDER_ARRAY) {
            if (index < MAX_FILE_NUMBER) {
                htmlStr_1 = appendFileItemHtmlStr(ORDER_ARRAY[index], htmlStr_1, index);
            } else if (index < MAX_FILE_NUMBER * 2) {
                htmlStr_2 = appendFileItemHtmlStr(ORDER_ARRAY[index], htmlStr_2, index);
            } else {
                htmlStr_3 = appendFileItemHtmlStr(ORDER_ARRAY[index], htmlStr_3, index);
            }
        }
        fileDiv_1.html(htmlStr_1);
        fileDiv_2.html(htmlStr_2);
        fileDiv_3.html(htmlStr_3);
    }

    /**
     * 初始化分页
     * @param divid 分页div ID
     * @param page 当前页
     * @param pageSize 每页最大条数
     * @param total 总页数
     * @param maxPageNumber 最大显示页码格式，不传默认为5
     */
    function initPaging(divid, page, pageSize, total, maxPageNumber) {
        var node = $("#" + divid);
        node.data("page", page);
        node.data("pageSize", pageSize);
        node.data("total", total);
        node.data("maxPageNumber", maxPageNumber);
        maxPageNumber = (maxPageNumber == null || maxPageNumber == undefined || maxPageNumber < 0) ? 5 : maxPageNumber;
        var totalPage = (total % pageSize == 0) ? parseInt(total / pageSize) : (parseInt(total / pageSize) + 1);
        var htmlStr = "<div class='my-paging-count'>\n" +
            "                    跳转<input type='text' class='form-control'" +
            "onkeyup=\"value = value.replace(/[^0-9]/g,'')\" totalPage='" + totalPage + "' onblur='fnSkipPage(this)' />页&nbsp;&nbsp;&nbsp;共" + totalPage + "页</div>";
        htmlStr += "<div class='my-paging-count my-trailerpage' pagingId='" + divid + "' onclick='fnTrailerpageClick(this)'>尾页</div>";
        htmlStr += "<div class='my-paging-class'>";
        htmlStr += "<li class='my-previous-li'>\n" +
            "                        <span>&laquo;</span>\n" +
            "                    </li>";
        // 开始显示页码号
        var startPageNum;
        // 结束显示页码号
        var stopPageNum = maxPageNumber;
        // 显示标识
        var diffPage = (maxPageNumber % 2 == 0) ? parseInt(maxPageNumber / 2) : (parseInt(maxPageNumber / 2) + 1);
        if (page - diffPage <= 1) {
            startPageNum = 1;
        } else if (page < totalPage - maxPageNumber + 1) {
            htmlStr += "<li>...</li>";
            startPageNum = page - 1;
        } else {
            if (totalPage != maxPageNumber) {
                htmlStr += "<li>...</li>";
            }
            startPageNum = totalPage - maxPageNumber + 1;
        }
        if (page != 1 && page == totalPage - maxPageNumber + 1) {
            startPageNum -= 1;
        }
        if (parseInt(page) + parseInt(diffPage) >= totalPage) {
            stopPageNum = totalPage;
        } else {
            stopPageNum = startPageNum + maxPageNumber - 1;
        }
        for (var i = startPageNum; i <= stopPageNum; i++) {
            if (i == page) {
                htmlStr += "<li pagingId='" + divid + "' class='pageItem pageItemAction'>" + i + "</li>";
            } else {
                htmlStr += "<li pagingId='" + divid + "' class='pageItem'>" + i + "</li>";
            }
        }
        if (stopPageNum != totalPage) {
            htmlStr += "<li>...</li>";
        }
        // 判断当前页位置
        htmlStr += "<li class='my-next-li'>\n" +
            "                        <span>&raquo;</span>\n" +
            "                    </li>";
        htmlStr += "</div>";
        htmlStr += "<div class='my-paging-count my-homepage' pagingId='" + divid + "' onclick='fnHomepageClick(this)'>首页</div>";
        htmlStr += "<div class='my-paging-count'>共" + total + "条</div>";
        node.html(htmlStr);
        $("#" + divid + " .pageItem").click(function () {
            var node = $(this);
            var thisPage = parseInt(node.text());
            if (thisPage != page) {
                // initPaging("pagingDiv", thisPage, pageSize, total, maxPageNumber);
                loadingFiles(thisPage);
            }
        });
    }

    // 首页
    function fnHomepageClick(obj) {
        var pagingId = $(obj).attr("pagingId");
        var pagingDiv = $("#" + pagingId);
        var page = pagingDiv.data("page");
        if (page != 1) {
            loadingFiles(1);
            // initPaging("pagingDiv", 1, pagingDiv.data("pageSize"), pagingDiv.data("total"), pagingDiv.data("maxPageNumber"));
        }
    }

    // 尾页
    function fnTrailerpageClick(obj) {
        var pagingId = $(obj).attr("pagingId");
        var pagingDiv = $("#" + pagingId);
        var page = pagingDiv.data("page");
        var total = pagingDiv.data("total");
        var pageSize = pagingDiv.data("pageSize");
        var totalPage = (total % pageSize == 0) ? parseInt(total / pageSize) : (parseInt(total / pageSize) + 1);
        if (page != totalPage) {
            loadingFiles(totalPage);
            // initPaging("pagingDiv", totalPage, pageSize, total, pagingDiv.data("maxPageNumber"));
        }
    }

    // 跳转
    function fnSkipPage(obj) {
        var node = $(obj);
        var val = node.val();
        if (val != null && val != "" && !isNaN(val)) {
            var totalPage = parseInt(node.attr("totalPage"));
            val = val > totalPage ? totalPage : val;
            val = val < 1 ? 1 : val;
            loadingFiles(val);
        }
        node.val("");
    }

    // 新增文件
    function fnInsertFile() {
        Base.openWindow("insertFile", "Insert File", "userFileManagerController!getInsertFilePage.do", null, "40%", "60%", function () {
            // 加载用户文件列表
            queryFiles();
        });
    }

    // 设置 鼠标移入元素，显示加号按钮效果
    function installMouseShowAdd() {
        // 获取所有要设置该效果的元素：必须保证每个元素下的子元素包含加号按钮
        $("#myDirectoryTitle,.item").hover(
            function () {
                $(this).children(".button-span").removeClass("hidden");
                $(this).children(".button-span").addClass("show");
            },
            function () {
                $(this).children(".button-span").removeClass("show");
                $(this).children(".button-span").addClass("hidden");
            }
        );
    }

    var USER_CATALOGUES = [];

    // 加载用户文件目录
    function initUserCatalogues() {
        $("#left_context").html("");
        Base.submit("", "userFileManagerController!queryUserCatalogues.do", null, null, function (data) {
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
                                "<span title='修改' class='pull-right circle-border button-span hidden' catalogueName='" + item["catalogueName"] +
                                "' catalogueId='" + item["id"] + "' onclick='updateCatalogues(this)'>/</span>" +
                                "<span title='删除' class='pull-right circle-border button-span hidden' catalogueName='" + item["catalogueName"] +
                                "' catalogueId='" + item["id"] + "' onclick='deleteCatalogues(this)'>-</span>" +
                                "<span title='新增' class='pull-right circle-border button-span hidden' catalogueName='" + item["catalogueName"] +
                                "' catalogueId='" + item["id"] + "' onclick='addCatalogues(this)'>+</span>" +
                                "</div>";
                            htmlStr += getHtmlByItem(item, list, 1);
                        }
                    }
                }
            }
            $("#left_context").html(htmlStr);
            // 设置 鼠标移入元素，显示加号按钮效果
            installMouseShowAdd();
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
                    "<span title='修改' class='pull-right circle-border button-span hidden' catalogueName='" + item["catalogueName"] +
                    "' catalogueId='" + item["id"] + "' onclick='updateCatalogues(this)'>/</span>" +
                    "<span title='删除' class='pull-right circle-border button-span hidden' catalogueName='" + item["catalogueName"] +
                    "' catalogueId='" + item["id"] + "' onclick='deleteCatalogues(this)'>-</span>" +
                    "<span title='新增' class='pull-right circle-border button-span hidden' catalogueName='" + item["catalogueName"] +
                    "' catalogueId='" + item["id"] + "' onclick='addCatalogues(this)'>+</span>" +
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

    var MAX_FILE_NUMBER = 0;
    var CATALOGUEID = "";

    // 加载用户文件列表: 根据目录id查询该目录及该目录的子目录的所有文件
    function queryFiles(obj) {
        CATALOGUEID = $(obj).attr("catalogueId");
        $("#iframeDiv").removeClass("show").addClass("hidden");
        $("#fileDiv").removeClass("hidden").addClass("show");
        $("#pagingDiv").removeClass("hidden").addClass("show");
        // 计算页面可用高度，动态设置
        var height = $("#leftDiv").height();
        var max = parseInt(height / 35 - 3);
        MAX_FILE_NUMBER = max;
        loadingFiles(1);
        /*var fileDiv_1 = $("#fileDiv_1");
        var fileDiv_2 = $("#fileDiv_2");
        var fileDiv_3 = $("#fileDiv_3");
        fileDiv_1.html("");
        fileDiv_2.html("");
        fileDiv_3.html("");
        CATALOGUEID = $(obj).attr("catalogueId");
        var params = {
            "catalogueId": CATALOGUEID,
            "page": 1,
            "pageSize": MAX_FILE_NUMBER * 3
        };
        Base.submit("", "userFileManagerController!queryFiles.do", params, null, function (data) {
            if (data.code == "0") {
                var pageMsg = data.data;
                var fileMsgs = pageMsg.list;
                if (fileMsgs != undefined && fileMsgs != null && fileMsgs.length > 0) {
                    var htmlStr_1 = "";
                    var htmlStr_2 = "";
                    var htmlStr_3 = "";
                    for (var index in fileMsgs) {
                        if (index < max) {
                            htmlStr_1 = appendFileItemHtmlStr(fileMsgs[index], htmlStr_1, index);
                        } else if (index < max * 2) {
                            htmlStr_2 = appendFileItemHtmlStr(fileMsgs[index], htmlStr_2, index);
                        } else {
                            htmlStr_3 = appendFileItemHtmlStr(fileMsgs[index], htmlStr_3, index);
                        }
                    }
                    fileDiv_1.html(htmlStr_1);
                    fileDiv_2.html(htmlStr_2);
                    fileDiv_3.html(htmlStr_3);
                }
                initPaging("pagingDiv", 1, MAX_FILE_NUMBER * 3, pageMsg.total);
            }
        });*/
    }

    // 渲染文件列表
    function loadingFiles(page) {
        var fileDiv_1 = $("#fileDiv_1");
        var fileDiv_2 = $("#fileDiv_2");
        var fileDiv_3 = $("#fileDiv_3");
        var htmlStr_1 = "";
        var htmlStr_2 = "";
        var htmlStr_3 = "";
        fileDiv_1.html(htmlStr_1);
        fileDiv_2.html(htmlStr_2);
        fileDiv_3.html(htmlStr_3);
        var params = {
            "catalogueId": CATALOGUEID,
            "page": page,
            "pageSize": MAX_FILE_NUMBER * 3
        };
        Base.submit("", "userFileManagerController!queryFiles.do", params, null, function (data) {
            if (data.code == "0") {
                var pageMsg = data.data;
                var fileMsgs = pageMsg.list;
                if (fileMsgs != undefined && fileMsgs != null && fileMsgs.length > 0) {
                    ORDER_ARRAY = fileMsgs;
                    // 排序方式排序
                    fnOrder();
                    for (var index in ORDER_ARRAY) {
                        if (index < MAX_FILE_NUMBER) {
                            htmlStr_1 = appendFileItemHtmlStr(ORDER_ARRAY[index], htmlStr_1, index);
                        } else if (index < MAX_FILE_NUMBER * 2) {
                            htmlStr_2 = appendFileItemHtmlStr(ORDER_ARRAY[index], htmlStr_2, index);
                        } else {
                            htmlStr_3 = appendFileItemHtmlStr(ORDER_ARRAY[index], htmlStr_3, index);
                        }
                    }
                    fileDiv_1.html(htmlStr_1);
                    fileDiv_2.html(htmlStr_2);
                    fileDiv_3.html(htmlStr_3);
                }
                initPaging("pagingDiv", page, MAX_FILE_NUMBER * 3, pageMsg.total);
            }
        });
    }

    // 根据排序方式进行排序
    function fnOrder() {
        var len = ORDER_ARRAY.length;
        if (len > 0) {
            var sign;
            // 排序方式：timeDown-时间降序 timeUp-时间升序 nameDown-名称降序 nameUp-时间升序
            switch (ORDER_VALUE) {
                case "timeDown":
                    for (var i = 0; i < len; i++){
                        $.each(ORDER_ARRAY, function (j, node) {
                            if (ORDER_ARRAY[i].addTime > node.addTime){
                                sign = ORDER_ARRAY[i];
                                ORDER_ARRAY[i] = ORDER_ARRAY[j];
                                ORDER_ARRAY[j] = sign;
                            }
                        });
                    }
                    break;
                case "timeUp":
                    for (var i = 0; i < len; i++){
                        $.each(ORDER_ARRAY, function (j, node) {
                            if (ORDER_ARRAY[i].addTime < node.addTime){
                                sign = ORDER_ARRAY[i];
                                ORDER_ARRAY[i] = ORDER_ARRAY[j];
                                ORDER_ARRAY[j] = sign;
                            }
                        });
                    }
                    break;
                case "nameDown":
                    for (var i = 0; i < len; i++){
                        $.each(ORDER_ARRAY, function (j, node) {
                            if (ORDER_ARRAY[i].fileName < node.fileName){
                                sign = ORDER_ARRAY[i];
                                ORDER_ARRAY[i] = ORDER_ARRAY[j];
                                ORDER_ARRAY[j] = sign;
                            }
                        });
                    }
                    break;
                case "nameUp":
                    for (var i = 0; i < len; i++){
                        $.each(ORDER_ARRAY, function (j, node) {
                            if (ORDER_ARRAY[i].fileName > node.fileName){
                                sign = ORDER_ARRAY[i];
                                ORDER_ARRAY[i] = ORDER_ARRAY[j];
                                ORDER_ARRAY[j] = sign;
                            }
                        });
                    }
                    break;
            }
        }
    }

    // 拼接文件item div
    function appendFileItemHtmlStr(item, htmlStr, index) {
        htmlStr += "<div class='fileItemDiv' title='" + item.fileName + "'>";
        // htmlStr += "<span>" + (parseInt(index) + 1) + ".</span>";
        htmlStr += "<a filePreviewUrl='" + item.filePreviewUrl + "' onclick='fnFileClick(this)'>" + (parseInt(index) + 1) + "." + item.fileName + "</a>";
        htmlStr += "<li filename='" + item.fileName + "' fileid='" + item.fileId + "' onclick='fnFileDeleteClick(this)'>×</li>";
        htmlStr += "</div>";
        return htmlStr;
    }

    // 文件删除按钮点击事件
    function fnFileDeleteClick(obj, e) {
        var fileId = $(obj).attr("fileid");
        // 阻止事件冒泡
        window.event ? window.event.cancelBubble = true : e.stopPropagation();
        var flag = confirm("是否删除" + $(obj).attr("filename") + "?");
        if (flag) {
            var params = {
                "fileId": fileId
            };
            Base.submit("", "userFileManagerController!deleteFileById.do", params, null, function (data) {
                if (data.code == "0") {
                    var params = {
                        "catalogueId": CATALOGUEID,
                        "page": 1,
                        "pageSize": MAX_FILE_NUMBER * 3
                    };
                    Base.submit("", "userFileManagerController!queryFiles.do", params, null, function (data) {
                        if (data.code == "0") {
                            var fileDiv_1 = $("#fileDiv_1");
                            var fileDiv_2 = $("#fileDiv_2");
                            var fileDiv_3 = $("#fileDiv_3");
                            var htmlStr_1 = "";
                            var htmlStr_2 = "";
                            var htmlStr_3 = "";
                            fileDiv_1.html(htmlStr_1);
                            fileDiv_2.html(htmlStr_2);
                            fileDiv_3.html(htmlStr_3);
                            var pageMsg = data.data;
                            var fileMsgs = pageMsg.list;
                            for (var index in fileMsgs) {
                                if (index < MAX_FILE_NUMBER) {
                                    htmlStr_1 = appendFileItemHtmlStr(fileMsgs[index], htmlStr_1, index);
                                } else if (index < MAX_FILE_NUMBER * 2) {
                                    htmlStr_2 = appendFileItemHtmlStr(fileMsgs[index], htmlStr_2, index);
                                } else {
                                    htmlStr_3 = appendFileItemHtmlStr(fileMsgs[index], htmlStr_3, index);
                                }
                            }
                            fileDiv_1.html(htmlStr_1);
                            fileDiv_2.html(htmlStr_2);
                            fileDiv_3.html(htmlStr_3);
                        }
                        initPaging("pagingDiv", 1, MAX_FILE_NUMBER * 3, pageMsg.total);
                    });
                } else {
                    alert(data.message);
                }
            });
        }

    }

    // 拼接文件item div
    function appendFileItemHtmlStr_1(item, htmlStr, index) {
        htmlStr += "<div class='fileItemDiv_1' title='" + item.fileName + "'>";
        htmlStr += "<span>" + (parseInt(index) + 1) + ".</span>";
        htmlStr += "<a filePreviewUrl='" + item.filePreviewUrl + "' onclick='fnFileClick_1(this)'>" + item.fileName + "</a>";
        htmlStr += "</div>";
        return htmlStr;
    }

    // 文件点击事件
    function fnFileClick(obj) {
        // 隐藏 fileDiv
        $("#fileDiv").removeClass("show").addClass("hidden");
        $("#pagingDiv").removeClass("show").addClass("hidden");
        $("#iframeDiv").removeClass("hidden").addClass("show");
        document.getElementById("filepreviewWin").src = "${basePath}" + $(obj).attr("filePreviewUrl");
        winRightDivClick(1, MAX_FILE_NUMBER);
    }

    // 文件点击事件
    function fnFileClick_1(obj) {
        // 隐藏 fileDiv
        $("#fileDiv").removeClass("show").addClass("hidden");
        $("#pagingDiv").removeClass("show").addClass("hidden");
        $("#iframeDiv").removeClass("hidden").addClass("show");
        document.getElementById("filepreviewWin").src = "${basePath}" + $(obj).attr("filePreviewUrl");
    }

    var WINRIGHT_PAGESIZE;

    // 右边div 查看更多点击事件
    function winRightDivClick(page, pageSize) {
        var winRightDiv = $("#filepreviewWinRightDiv");
        winRightDiv.html("");
        WINRIGHT_PAGESIZE = pageSize;
        var params = {
            "catalogueId": CATALOGUEID,
            "page": page,
            "pageSize": WINRIGHT_PAGESIZE
        };
        Base.submit("", "userFileManagerController!queryFiles.do", params, null, function (data) {
            if (data.code == "0") {
                var pageMsg = data.data;
                var total = pageMsg.total;
                var fileMsgs = pageMsg.list;
                if (fileMsgs != undefined && fileMsgs != null && fileMsgs.length > 0) {
                    var htmlStr_1 = "";
                    for (var index in fileMsgs) {
                        htmlStr_1 = appendFileItemHtmlStr_1(fileMsgs[index], htmlStr_1, index);
                    }
                    if (pageSize <= total) {
                        htmlStr_1 += "<div class='viewMore' title='点击查看更多' onclick='winRightDivClick(1, parseInt(MAX_FILE_NUMBER) + parseInt(WINRIGHT_PAGESIZE))'>>>>查看更多>>></div>";
                    }
                    winRightDiv.html(htmlStr_1);
                }
            }
        });
    }

    // 点击添加按钮事件
    function addCatalogues(obj, e) {
        // 阻止事件冒泡
        window.event ? window.event.cancelBubble = true : e.stopPropagation();
        var catalogueId = $(obj).attr("catalogueId");
        var catalogueName = $(obj).attr("catalogueName");
        var params = {
            "catalogueId": catalogueId,
            "catalogueName": catalogueName
        };
        Base.openWindow("addDirectory", "Add Directory", "userFileManagerController!getAddDirectoryPage.do", params, "60%", "60%", function () {
            // 加载用户文件目录
            initUserCatalogues();
        });
    }

    // 点击修改按钮事件
    function updateCatalogues(obj, e) {
        // 阻止事件冒泡
        window.event ? window.event.cancelBubble = true : e.stopPropagation();
        var catalogueId = $(obj).attr("catalogueId");
        var catalogueName = $(obj).attr("catalogueName");
        var params = {
            "catalogueId": catalogueId,
            "catalogueName": catalogueName
        };
        Base.openWindow("updateDirectory", "Update Directory", "userFileManagerController!getUpdateDirectoryPage.do", params, "60%", "60%", function () {
            // 加载用户文件目录
            initUserCatalogues();
        });
    }

    // 点击删除按钮事件
    function deleteCatalogues(obj, e) {
        // 阻止事件冒泡
        window.event ? window.event.cancelBubble = true : e.stopPropagation();
        var catalogueId = $(obj).attr("catalogueId");
        var catalogueName = $(obj).attr("catalogueName");
        var flag = confirm("是否删除" + catalogueName + "?");
        if (flag) {
            var params = {
                "catalogueId": catalogueId
            };
            Base.submit("", "userFileManagerController!deleteDirectory.do", params, null, function (data) {
                if (data.code == "0") {
                    // 加载用户文i件目录
                    initUserCatalogues();
                } else {
                    alert(data.message);
                }
            });
        }
    }
</script>
</html>
