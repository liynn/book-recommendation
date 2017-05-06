$(document).ready(function () {
    var modelProgressTimer;
    /**
     * 登录按钮点击事件
     */
    $('#login').on('click', function () {
        var phone = $('#phone').val();
        var password = $('#password').val();
        $.ajax({
                   type: 'POST',
                   url: 'user/login',
                   data: {phone: phone, password: password},
                   dataType: 'json',
                   success: function (response) {
                       if (response.success) {
                           window.location.reload();
                       } else {
                           $('#loginTip').html(response.error);
                       }
                   }
               });
    });
    /**
     * 手机号码失去焦点事件
     */
    $('#telephone').on('blur', function () {
        $('#registerTip').html('');
        //手机号验证规则
        var phoneReg = /^(((13[0-9]{1})|(14[0-9]{1})|(17[0]{1})|(15[0-3]{1})|(15[5-9]{1})|(18[0-9]{1}))+\d{8})$/;
        var phone = $('#telephone').val();
        if (!phoneReg.test(phone)) {
            $('#registerTip').html('手机号码格式不正确');
            $('#telephone').focus();
        }
    });
    /**
     * 电子邮箱失去焦点事件
     */
    $('#email').on('blur', function () {
        $('#registerTip').html('');
        //电子邮箱校验规则
        var emailReg = /^[a-z\d]+(\.[a-z\d]+)*@([\da-z](-[\da-z])?)+(\.{1,2}[a-z]+)+$/;
        var email = $('#email').val();
        if (!emailReg.test(email)) {
            $('#registerTip').html('邮箱格式不正确');
            $('#email').focus();
        }
    });
    /**
     * 年龄失去焦点事件
     */
    $('#age').on('blur', function () {
        $('#registerTip').html('');
        //年龄校验规则
        var ageReg = /^(\+|-)?\d+$/;
        var age = $('#age').val();
        if (!ageReg.test(age) || age < 0) {
            $('#registerTip').html('年龄输入不正确');
            $('#age').focus();
        }
    });
    /**
     * 注册按钮点击事件
     */
    $('#register').on('click', function () {
        var phone = $('#telephone').val();
        var email = $('#email').val();
        var name = $('#username').val().trim();
        var sex = $('input:radio[name="sex"]:checked').val();
        var age = $('#age').val();
        var password = $('#newPassword').val();
        var confirmPassword = $('#confirmPassword').val();
        var param = '{phone:"' + phone + '",email:"' + email + '",name:"' + name + '",sex:"' + sex
                    + '",age:' + age + ',password:"' + password + '",confirmPassword:"'
                    + confirmPassword + '"}';
        $.ajax({
                   type: 'POST',
                   url: 'user/register',
                   data: param,
                   dataType: 'json',
                   contentType: "application/json;charset=utf-8",
                   success: function (response) {
                       if (response.success) {
                           $('#registerModal').modal('hide');
                           $('#tipId').html('注册用户成功(^_^)');
                           openModal('tipModal');
                       } else {
                           $('#registerTip').html(response.error);
                       }
                   }
               });
    });
    /**
     * 登出按钮点击事件
     */
    $('#logout').on('click', function () {
        $.ajax({
                   type: 'GET',
                   url: 'user/logout',
                   success: function (response) {
                       if (response.success) {
                           window.location.reload();
                       }
                   }
               });
    });

    /**
     * 图书大厅点击事件
     */
    $('#bookHall').on('click', function () {
        getAllBook(null, null, 1, 10);
    });
    /**
     * 搜索按钮点击事件
     */
    $('#search').on('click', function () {
        var bookId = $('#bookId').val();
        var name = $('#name').val();
        if (bookId == '') {
            bookId = null;
        }
        if (name == '') {
            name = null;
        }
        getAllBook(bookId, name, 1, 10);
    });
    /**
     * 评分点击事件
     */
    $('#hall').on('click', '.starImg0', function () {
        $(this).parent().parent().find('td').eq(0).find('input:checkbox:first')
            .attr('checked', 'true');
        var star = updateScore(1);
        $(this).parent().html(star);
    });
    $('#hall').on('click', '.starImg1', function () {
        $(this).parent().parent().find('td').eq(0).find('input:checkbox:first')
            .attr('checked', 'true');
        var star = updateScore(2);
        $(this).parent().html(star);
    });

    $('#hall').on('click', '.starImg2', function () {
        $(this).parent().parent().find('td').eq(0).find('input:checkbox:first')
            .attr('checked', 'true');
        var star = updateScore(3);
        $(this).parent().html(star);
    });

    $('#hall').on('click', '.starImg3', function () {
        $(this).parent().parent().find('td').eq(0).find('input:checkbox:first')
            .attr('checked', 'true');
        var star = updateScore(4);
        $(this).parent().html(star);
    });

    $('#hall').on('click', '.starImg4', function () {
        $(this).parent().parent().find('td').eq(0).find('input:checkbox:first')
            .attr('checked', 'true');
        var star = updateScore(5);
        $(this).parent().html(star);
    });

    /**
     * 提交评分点击事件
     */
    $('#submitRating').on('click', function () {
        //获取当前登录用户编号
        var userId = $('#currentUserId').val();
        console.log(userId.length);
        if (userId) {
            var param = '{"userId":' + userId + ',"ratings":[';
            $('#hall').children('tr').each(function () {
                if ($(this).find('td').eq(0).find('input:checkbox:first').is(':checked')) {
                    var bookId = $(this).find('td').eq(1).html();
                    var score = $(this).find('td').eq(6)
                        .find('img[src="static/images/star_orange.png"]').length;
                    param += '{"bookId":' + bookId + ',"score":' + score + '},';
                }
            });
            param = param.substring(0, param.length - 1);
            param += ']}';
            $.ajax({
                       type: 'POST',
                       url: 'book/addRatings',
                       dataType: 'json',
                       data: param,
                       contentType: "application/json;charset=utf-8",
                       success: function (response) {
                           if (response.success) {
                               $('#tipId').html('成功添加图书评分(^_^)');
                               openModal('tipModal');
                           }
                       }
                   });
        } else {
            $('#tipId').html('用户未登录');
            openModal('tipModal');
        }
    });

    /**
     * 建立模型点击事件
     */
    $('#modelBtn').on('click', function () {
        //开启进度条，初始值为0%
        setProgress('progressId', '0%');
        openModal('progressModal');
        //获取输入参数
        var rank = $('#rank').val();
        var lambda = $('#lambda').val();
        var iterator = $('#iterator').val();
        //发送AJAX请求建立推荐模型
        $.ajax({
                   url: 'book/model',
                   type: 'POST',
                   async: false,
                   data: {rank: rank, lambda: lambda, iterator: iterator},
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
        if (value >= 0 && value <= 20) {
            $('#' + id).addClass("progress-bar-danger");
        } else if (value >= 20 && value <= 40) {
            $('#' + id).removeClass("progress-bar-danger");
            $('#' + id).addClass("progress-bar-warning");
        } else if (value >= 40 && value <= 60) {
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
                                       '<img src="static/images/star_orange.png" style="margin-top: -5px"/>';
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
/**
 * 更新评分
 * @param score 分数
 */
function updateScore(score) {
    var star = '';
    for (var i = 0; i < score; i++) {
        star +=
            '<img class="starImg' + i + '" src="static/images/star_orange.png"/>';
    }
    for (var i = score; i < 5; i++) {
        star +=
            '<img class="starImg' + i + '" src="static/images/star_gray.png"/>';
    }
    return star;
}

/**
 * 获取图书信息
 */
function getAllBook(bookId, name, pageNo, pageSize) {
    $.ajax({
               type: 'GET',
               url: 'book/paging',
               dataType: 'json',
               data: {bookId: bookId, name: name, pageNo: pageNo, pageSize: pageSize},
               success: function (response) {
                   $('#hall').html('');
                   var temp = '';
                   $.each(response.result.data, function (index, content) {
                       var star = '';
                       for (var i = 0; i < 5; i++) {
                           star +=
                               '<img class="starImg' + i + '" src="static/images/star_gray.png"/>';
                       }
                       temp += '<tr><td><input type="checkbox"></td><td>'
                               + content.id + '</td> <td style="color: #3377aa">'
                               + content.name + '</td><td>'
                               + content.author + '</td><td>'
                               + content.publisher + '</td><td>'
                               + content.price + '</td><td>'
                               + star + '</td></tr>'

                   });
                   $('#hall').append(temp);

                   var pageNumber = response.result.pageNumber;
                   var pageNo = response.result.pageNo;
                   var pageSize = response.result.pageSize;

                   var options = {
                       bootstrapMajorVersion: 3, //版本
                       currentPage: pageNo, //当前页数
                       totalPages: pageNumber, //总页数
                       numberOfPages: pageSize, //页面大小
                       itemTexts: function (type, page, current) {
                           switch (type) {
                               case "first":
                                   return "首页";
                               case "prev":
                                   return "上一页";
                               case "next":
                                   return "下一页";
                               case "last":
                                   return "末页";
                               case "page":
                                   return page;
                           }
                       },
                       onPageClicked: function (event, originalEvent, type, page) {
                           $('#hall').html('');
                           $.ajax({
                                      type: 'GET',
                                      url: 'book/paging',
                                      dataType: 'json',
                                      data: {
                                          bookId: bookId,
                                          name: name,
                                          pageNo: page,
                                          pageSize: pageSize
                                      },
                                      success: function (response) {
                                          var temp = '';
                                          $.each(response.result.data, function (index, content) {
                                              var star = '';
                                              for (var i = 0; i < 5; i++) {
                                                  star +=
                                                      '<img class="starImg' + i
                                                      + '" src="static/images/star_gray.png"/>';
                                              }
                                              temp += '<tr><td><input type="checkbox"></td> <td>'
                                                      + content.id
                                                      + '</td> <td style="color: #3377aa">'
                                                      + content.name + '</td><td>'
                                                      + content.author + '</td><td>'
                                                      + content.publisher + '</td><td>'
                                                      + content.price + '</td><td id = >'
                                                      + star + '</td></tr>'

                                          });
                                          $('#hall').append(temp);
                                      }
                                  });
                       }
                   };
                   $('#page').bootstrapPaginator(options);
               }
           });
}

