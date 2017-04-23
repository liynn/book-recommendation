$(document).ready(function () {
    var modelProgressTimer;
    /**
     * 建立模型点击事件
     */
    $('#modelBtn').on('click', function () {
        //开启进度条，初始值为0%
        setProgress('progressId', '0%');
        openModal('progressModal');
        //获取输入参数
        var address = $('#address').val();
        var rank = $('#rank').val();
        var lambda = $('#lambda').val();
        var iterator = $('#iterator').val();
        //发送AJAX请求建立推荐模型
        $.ajax({
                   url: 'book/model',
                   type: 'POST',
                   async: false,
                   data: {address: address, rank: rank, lambda: lambda, iterator: iterator},
                   dateType: 'json',
                   success: function (data) {
                       if (data == null) {
                           $('#modelId').html = "调用建模失败!";
                           openModal("modelModal");
                           return;
                       }
                       modelProgressTimer = setInterval(function () {
                           queryTaskProgress(data)
                       }, 1000);
                   }
               });

    });

    /**
     * 设置进度条
     * @param id
     * @param value
     */
    function setProgress(id, value) {
        $('#' + id).css("width", value).text(value);
        value = parseInt(value.replace('%', ''));
        if (value >= 0 && value <= 30) {
            $('#' + id).addClass("progress-bar-danger");
        } else if (value >= 30 && value <= 60) {
            $('#' + id).removeClass("progress-bar-danger");
            $('#' + id).addClass("progress-bar-warning");
        } else if (value >= 60 && value <= 90) {
            $('#' + id).removeClass("progress-bar-warning");
            $('#' + id).addClass("progress-bar-info");
        } else {
            $('#' + id).removeClass("progress-bar-info");
            $('#' + id).addClass("progress-bar-success");
        }
    }

    /**
     * 请求任务进度
     */
    function queryTaskProgress(appId) {
        $.ajax({
                   type: 'GET',
                   url: 'model/progress',
                   dataType: 'json',
                   data: {appId: appId},
                   error: function (data) {
                       var result = data.responseText.toString();
                       if (result.indexOf("%") == -1) {
                           setProgress('progressId', '100%');
                           // 关闭计时器
                           clearInterval(modelProgressTimer);
                           // 关闭弹窗进度条
                           $('#progressModal').modal('hide');
                           // 开启提示条模态框
                           $('#modelId').html(
                               result == "FINISHED" ? "模型训练完成!" : (data == "FAILED" ? "调用建模失败!"
                                   : "模型训练被杀死!"));

                           openModal('modelModal');
                           return;
                       }
                       setProgress('progressId', result);
                   }
               });
    }

    /**
     * 用户信息查询点击事件
     */
    $('#userBtn').on('click', function () {
        $('#recommendLabel').hide();
        $('#recommendBook').html("");
        $('#recommendBook').hide();
        //获取输入用户编号
        var userId = $('#userId').val();
        //发送AJAX请求
        $.ajax({
                   type: 'GET',
                   url: 'user/detail',
                   dataType: 'json',
                   data: {userId: userId},
                   success: function (response) {
                       $('#userLabel').hide();
                       $('#userBaseInfo').html("");
                       $('#userBaseInfo').hide();
                       $('#ratingLabel').hide();
                       $('#ratingBook').html("");
                       $('#ratingBook').hide();
                       if (response.success) {
                           //用户基本信息组装
                           var userInfo = '<tr><td style="width: 375px;">编号:'
                                          + response.result.userId
                                          + '</td><td style="width: 375px;">姓名:'
                                          + response.result.name
                                          + '</td><td style="width: 375px;">性别:'
                                          + response.result.sex
                                          + '</td></tr><tr><td style="width: 375px;">年龄:'
                                          + response.result.age
                                          + '</td><td style="width: 375px;">电话:'
                                          + response.result.phone
                                          + '</td><td style="width: 375px;">邮箱:'
                                          + response.result.email
                                          + '</td></tr>';
                           //用户评分信息组装
                           var $bookList = response.result.bookList;
                           var temp = '';
                           $.each($bookList, function (index, content) {
                               var star = '';
                               for (var i = 0; i < parseInt(content.score); i++) {
                                   star +=
                                       '<img src="static/images/star.png" style="margin-top: -5px"/>';
                               }
                               if (index % 3 == 0) {
                                   temp += '<tr>'
                               }
                               temp += '<td valign="top" style="padding: 10px; width:375px;">'
                                       + '<img src="' + content.image
                                       + '"style="float:left;width: 120px">'
                                       + '<div style="float: left;width: 222px;margin-left: 10px;">'
                                       + '<span style="color: #3377aa;">'
                                       + content.name + '</span><br/><br/>'
                                       + '<span>作者:'
                                       + content.author + '</span><br/>'
                                       + '<span>出版社:'
                                       + content.publisher + '</span><br/>'
                                       + '<span>价格:'
                                       + content.price + '</span><br/><br/>'
                                       + '<span>评分:'
                                       + star + '</span>'
                                       + '</div>'
                                       + '</td>';
                               if (index % 3 == 2) {
                                   temp += '</tr>'
                               }
                           });
                           //设置用户基本信息
                           $('#userBaseInfo').append(userInfo);
                           //设置用户评分信息
                           $('#ratingBook').append(temp);
                           $("#ratingBook tr").each(function () {
                               var len = $(this).children("td").length;
                               if (len == 1) {
                                   $(this).append('<td></td><td></td>');
                               }
                               if (len == 2) {
                                   $(this).append('<td></td>');
                               }
                           });
                           $('#userLabel').show();
                           $('#userBaseInfo').show();
                           $('#ratingLabel').show();
                           $('#ratingBook').show();
                       } else {
                           $('#tipId').html(response.error);
                           openModal('tipModal');
                       }
                   }
               });
    });

    /**
     *  推荐按钮点击事件
     */
    $('#recommendBtn').on('click', function () {
        var dialog = $('#recommendModal').find('.modal-dialog');
        dialog.css({'margin-top': 360});
        $('#recommendModal').modal('show');
        //获取用户编号
        var userId = $('#userId').val();
        //获取推荐数目
        var number = $('#number').val();
        //发送AJAX请求
        $.ajax({
                   type: 'GET',
                   url: 'book/recommend',
                   dataType: 'json',
                   data: {userId: userId, number: number},
                   success: function (response) {
                       $('#recommendModal').modal('hide');
                       $('#recommendLabel').hide();
                       $('#recommendBook').html("");
                       $('#recommendBook').hide();
                       if (response.success) {
                           var temp = '';
                           $.each(response.result, function (index, content) {
                               if (index % 3 == 0) {
                                   temp += '<tr>'
                               }
                               temp += '<td valign="top" style="padding: 10px; width:375px;">'
                                       + '<img src="' + content.image
                                       + '"style="float:left;width: 120px">'
                                       + '<div style="float: left;width: 222px;margin-left: 10px;">'
                                       + '<span style="color: #3377aa;">'
                                       + content.name + '</span><br/><br/>'
                                       + '<span>作者:'
                                       + content.author + '</span><br/>'
                                       + '<span>出版社:'
                                       + content.publisher + '</span><br/>'
                                       + '<span>价格:'
                                       + content.price + '</span><br/><br/>'
                                       + '<span>推荐分:<span style="color: #f0871e">'
                                       + content.score + '</span></span>'
                                       + '</div>'
                                       + '</td>';
                               if (index % 3 == 2) {
                                   temp += '</tr>'
                               }
                           });
                           //设置评分信息
                           $('#recommendBook').append(temp);
                           $("#recommendBook tr").each(function () {
                               var len = $(this).children("td").length;
                               if (len == 1) {
                                   $(this).append('<td></td><td></td>');
                               }
                               if (len == 2) {
                                   $(this).append('<td></td>');
                               }
                           });
                           $('#recommendLabel').show();
                           $('#recommendBook').show();
                       } else {
                           $('#tipId').html(response.error);
                           openModal('tipModal');
                       }
                   }
               });
    });
});

/**
 * 开启模态框
 * @param id
 */
function openModal(id) {
    $('#' + id).on('show.bs.modal', function () {
        var $this = $(this);
        var $modal_dialog = $this.find('.modal-dialog');
        $this.css('display', 'block');
        $modal_dialog.css(
            {'margin-top': Math.max(0, ($(window).height() - $modal_dialog.height()) / 2)});
    });
    $('#' + id).modal({backdrop: 'static', keyboard: false});
}
/**
 * 关闭模型进度条
 * @param id
 */
function closeModelModal(id) {
    $('#' + id).modal('hide');
    $('a[href="#result"]').tab('show');
}

/**
 * 关闭提示模态框
 * @param id
 */
function closeTipModal(id) {
    $('#' + id).modal('hide');
}


