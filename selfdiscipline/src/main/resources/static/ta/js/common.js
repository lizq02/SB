Base = new Object();

/**
 * 1.异步提交表单，action必须返回JSON或者null，此方法不能用于页面跳转，通常用于返回表格数据。
 * 2.能够自动校验表单
 * 3.能够对后台返回的json进行自动处理。
 * @param submitIds 需要传递到后台的对象id或容器id,多个id可以用","隔开
 * @param url 提交的地址
 * @param parameter 入参 json格式对象
 * @param onSubmit 提交前手动检查，如果返回false将不再提交,必须返回true或false
 * @param successCallback callback 返回业务成功后的回调，入参返回的为json对象和XMLHttpRequest对象
 *                     例如：function(data){alert(data.lists)}，其中data为返回的json数据
 * @param failCallback 业务失败回调，入参返回的为json对象和XMLHttpRequest对象
 * @param async (默认: true) 默认设置下，所有请求均为异步请求。
 *              如果需要发送同步请求，请将此选项设置为 false。注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
 * @param type (默认: "POST") 请求方式 ("POST" 或 "GET")， 默认为 "POST"。
 */
Base.submit = function (submitIds, url, parameter, onSubmit, successCallback, failCallback, async, type) {
    // 参数校验
    submitIds = (submitIds == null) ? "" : submitIds;
    url = (url == null) ? "" : url;
    parameter = (parameter == null || parameter.length == 0) ? {} : parameter;
    onSubmit = (onSubmit == null) ? function () {
        return true;
    } : onSubmit;
    async = (async == true) ? true : false;
    type = (async === "GET") ? "GET" : "POST";
    // 设置函数默认参数
    var settings = {
        submitIds: "",
        url: "",
        type: type,
        parameter: {},
        onSubmit: function () {
        },
        async: true,
        timeout: 10000,/*设置请求超时时间，毫秒*/
        context: document.body,/*这个对象用于设置Ajax相关回调函数的上下文。
                                也就是说，让回调函数内this指向这个对象（如果不设定这个参数，
                                那么this就指向调用本次AJAX请求时传递的options参数）。*/
        successCallback: function (data, request) {
        },
        failCallback: function (data, request) {
            alert("服务器内部错误,请联系系统管理员!")
        }
    };
    jQuery.extend(settings, {
        submitIds: submitIds,
        url: url,
        parameter: parameter,
        onSubmit: onSubmit,
        async: async,
        successCallback: successCallback,
        failCallback: failCallback,
        type: type
    });
    var data = parameter;
    if (settings.submitIds != null && settings.submitIds.length > 0) {
        var formIds = settings.submitIds.trim().split(",");
        for (var i = 0, len = formIds.length; i < len; i++) {
            var form = document.getElementById(formIds[i]);
            var items = form.elements;
            for (var j = 0, num = items.length; j < num; j++) {
                var key = $(items[j]).attr("id") || $(items[j]).attr("name");
                if (key == null || key == undefined) {
                    continue;
                }
                data[key] = $(items[j]).val();
            }
        }
    }
    jQuery.ajax({
        url: settings.url,
        data: data,
        dataType: "json",
        type: settings.type,
        async: settings.async,
        timeout: settings.timeout,
        context: settings.context,
        /*由服务器返回，并根据dataType参数进行处理后的数据；描述状态的字符串。*/
        success: function (data, textStatus, XMLHttpRequest) {
            // data 可能是 xmlDoc, jsonObj, html, text, 等等...
            settings.successCallback(data, XMLHttpRequest);
        },
        /*XMLHttpRequest 对象、错误信息、（可选）捕获的异常对象。
        如果发生了错误，错误信息（第二个参数）除了得到null之外，还可能是"timeout", "error", "notmodified" 和 "parsererror"。*/
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            settings.failCallback(data, XMLHttpRequest);
        }
    });
}

/**
 * msg string 消息内容
 * title string 对话框标题
 * callback function 返回函数。在隐藏并且CSS动画结束后触发
 **/
/*window.alert = function (msg, title, callback) {
    if (!title) {
        title = '对话框';
    }
    var dialogHTML = '<div id="selfAlert" class="modal fade">';
    dialogHTML += '<div class="modal-dialog">';
    dialogHTML += '<div class="modal-content">';
    dialogHTML += '<div class="modal-header">';
    dialogHTML += '<button type="button" class="close" data-dismiss="modal" aria-label="Close">';
    dialogHTML += '<span aria-hidden="true">&times;</span>';
    dialogHTML += '</button>';
    dialogHTML += '<h4 class="modal-title">' + title + '</h4>';
    dialogHTML += '</div>';
    dialogHTML += '<div class="modal-body">';
    dialogHTML += msg;
    dialogHTML += '</div>';
    dialogHTML += '<div class="modal-footer">';
    dialogHTML += '<button type="button" class="btn btn-primary" data-dismiss="modal">确定</button>';
    dialogHTML += '</div>';
    dialogHTML += '</div>';
    dialogHTML += '</div>';
    dialogHTML += '</div>';

    if ($('#selfAlert').length <= 0) {
        $('body').append(dialogHTML);
    }

    $('#selfAlert').on('hidden.bs.modal', function () {
        $('#selfAlert').remove();
        if (typeof callback == 'function') {
            callback();
        }
    }).modal('show');
}*/

/**
 * 在当前一个页面打开一个弹窗
 * @param id 窗口id
 * @param title 窗口标题（支持html格式字符串）
 * @param url url
 * @param parameter 参数（JSON格式，如{param1:'xxx',param2:'xxx'}）
 * @param width 窗口宽度
 * @param height 窗口高度
 * @param onClose 窗口关闭回调函数
 */
Base.openWindow = function(id, title, url, parameter, width, height, onClose) {
    // 获取当前窗口高度、宽度
    var documentHeight = $(document).height();
    var documentWidth = $(document).width();
    var windowHtmlStr = "<div id='" + id + "' class='ta-window-mc' + style='height:" + documentHeight + "px;'>";
    width = (width || "400px") + "";
    height = (height || "400px") + "";
    var top;
    if (height.indexOf("%") > -1) {
        var h = (parseInt(documentHeight) * parseInt(height)) / 100;
        top = (parseInt(documentHeight) - parseInt(h)) / 2;
    } else {
        top = (parseInt(documentHeight) - parseInt(height)) / 2;
    }
    var left;
    if (width.indexOf("%") > -1) {
        var w = (parseInt(documentWidth) * parseInt(width)) / 100;
        left = (parseInt(documentWidth) - parseInt(w)) / 2;
    } else {
        left = (parseInt(documentWidth) - parseInt(width)) / 2;
    }
    if (!isNaN(width)) {
        width += "px";
    }
    if (!isNaN(height)) {
        height += "px";
    }
    var nodeId = id + "_" + new Date().getTime();
    windowHtmlStr += "<div class='ta-window-div' style='height: " + height + ";width: " + width + ";top:" + top + "px;left:" + left + "px'>";
    windowHtmlStr += "<div>" +
        "            <div class='pull-left ta-window-title'>" + title + "</div>" +
        "            <div class='pull-right ta-window-close' name='" + nodeId + "'></div>" +
        "            <div class='pull-right ta-window-maximize' name='" + nodeId + "' status='min' historyHeight='" + height + "' historyWidth='" + width +
        "'           historyTop='" + top + "' historyLeft='" + left + "' onclick='windowMaximizeOnclick(this)'></div>" +
        "        </div>";
    var src = url;
    if (parameter != undefined && parameter != null){
        var index = 0;
        for (var key in parameter){
            if (index === 0){
                src += "?" + key + "=" + parameter[key];
            }else {
                src += "&" + key + "=" + parameter[key];
            }
        }
    }
    windowHtmlStr += "<iframe src='" + src + "' width='100%' height='100%'" +
        "frameborder='0' scrolling='auto'></iframe>";
    windowHtmlStr += "</div>";
    windowHtmlStr += "</div>";
    $("body").append(windowHtmlStr);
    $("#" + id + " .ta-window-close").click(function () {
        onClose();
        windowCloseOnclick(this);
    });
}

// 窗口放大缩小按钮点击事件
function windowMaximizeOnclick(obj) {
    obj = $(obj);
    var name = obj.attr("name");
    var id = name.substring(0, name.lastIndexOf("_"));
    var status = obj.attr("status");
    var node = $("#" + id + " .ta-window-div");
    if (status == "min"){
        // 放大
        node.attr("style","height:95%;width:100%;top:0px;left:0px;");
        obj.attr("status", "max");
    }else if (status == "max"){
        // 缩小
        $("#" + id + " .ta-window-div").attr("style",
            "height:" + obj.attr("historyHeight") + ";width:" + obj.attr("historyWidth") +
            ";top:" + obj.attr("historyTop") + "px;left:" + obj.attr("historyLeft") + "px;");
        obj.attr("status", "min");
    }
}

// 窗口关闭按钮点击事件
function windowCloseOnclick(obj) {
    var name = $(obj).attr("name");
    Base.closeWindow(name.substring(0, name.lastIndexOf("_")));
}

// 关闭窗口
Base.closeWindow = function(id) {
    document.getElementsByTagName("BODY").item(0).removeChild(document.getElementById(id));
}
