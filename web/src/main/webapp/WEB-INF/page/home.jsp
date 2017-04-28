<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>首页-图书推荐</title>
    <link href="${pageContext.request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/static/css/style.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/static/js/jquery-3.2.0.min.js"
            type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap.min.js"
            type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/bootstrap-paginator.min.js"
            type="text/javascript"></script>
    <script src="${pageContext.request.contextPath}/static/js/book.js"
            type="text/javascript"></script>

</head>
<body style="padding: 0;margin: 0;border: 0" onload="getAllBook(null, null, 1, 10)">
<nav class="navbar navbar-default"
     style="background-color: #3678ac;border-color: #105e89;height: 66px;">
    <div class="container-fluid" style="height: 65px;text-align: center">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"
               style="color: #ecf0f1;padding-left: 100px;font-size: 24px;line-height: 32px;">图书推荐系统</a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <%
                Integer userId = (Integer) session.getAttribute("userId");
                if (userId == null) {
            %>
            <li id="userInfo" style="padding-right: 10px;">
                <span>
                    <img src="${pageContext.request.contextPath}/static/images/register.png"
                         style="margin: 24px auto"/>
                    <a href="#" style="color: #ecf0f1;" data-toggle="modal"
                       data-target="#registerModal">注册</a>
                </span>
            </li>
            <li id="loginInfo" style="padding-right: 10px;">
                <span>
                    <img src="${pageContext.request.contextPath}/static/images/login.png"
                         style="margin: 24px auto"/>
                    <a href="#" style="color: #ecf0f1;" data-toggle="modal"
                       data-target="#loginModal">登录</a>
                </span>
            </li>
            <%} else { %>
            <li id="userInfo" style="padding-right: 10px;">
                <div style="margin: 21px auto; color: #ecf0f1">
                    当前登录人:<%=session.getAttribute("name")%>
                </div>
            </li>
            <li id="loginInfo" style="padding-right: 10px;">
                <span>
                    <img src="static/images/logout.png" style="margin: 24px auto"/>
                    <a id="logout" href="#" style="color: #ecf0f1;">登出</a>
                </span>
            </li>
            <%} %>
        </ul>
    </div>
    </div>
</nav>
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog"
     aria-labelledby="loginModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="loginModalLabel">账号登录</h4>
            </div>
            <div class="modal-body">
                <div style="text-align: center;">
                    <span id="loginTip" style="color: red"></span>
                </div>
                <div style="text-align: center;margin-top: 22px">
                    <label>手机号码：</label>
                    <input type="text" id="phone" class="form-control" placeholder="手机号码"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <div style="text-align: center">
                    <label>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
                    <input type="password" id="password" class="form-control" placeholder="密码"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <input type="hidden" id="currentUserId"
                       value="<%=userId%>"/>
                <div style="text-align: center">
                    <button type="button" id="login" class="btn btn-primary"
                            style="width: 300px;margin-left: 75px">登录
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="registerModal" tabindex="-1" role="dialog"
     aria-labelledby="registerModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    &times;
                </button>
                <h4 class="modal-title" id="registerModalLabel">账号注册</h4>
            </div>
            <div class="modal-body">
                <div style="text-align: center;">
                    <span id="registerTip" style="color: red"></span>
                </div>
                <div style="text-align: center;margin-top: 22px">
                    <label>手机号码：</label>
                    <input type="text" id="telephone" class="form-control" placeholder="手机号码"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <div style="text-align: center;">
                    <label>邮&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;箱：</label>
                    <input type="text" id="email" class="form-control" placeholder="邮箱"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <div style="text-align: center;">
                    <label>姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label>
                    <input type="text" id="username" class="form-control" placeholder="姓名"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <div style="margin-left: 98px;">
                    <label>性&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;别：</label>
                    <label class="checkbox-inline">
                        <input type="radio" name="sex" id="male" value="男" checked>男
                    </label>
                    <label class="checkbox-inline">
                        <input type="radio" name="sex" id="female" value="女">女
                    </label>
                </div>
                <br/>
                <div style="text-align: center;">
                    <label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;龄：</label>
                    <input type="text" id="age" class="form-control" placeholder="年龄"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <div style="text-align: center">
                    <label>密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
                    <input type="password" id="newPassword" class="form-control" placeholder="密码"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <div style="text-align: center">
                    <label>确认密码：</label>
                    <input type="password" id="confirmPassword" class="form-control"
                           placeholder="确认密码"
                           style="display:inline;width: 300px;">
                </div>
                <br/>
                <div style="text-align: center">
                    <button type="button" id="register" class="btn btn-success"
                            style="width: 300px;margin-left: 75px">注册
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="tipModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel2">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="alert alert-info" style="margin-bottom: 0px;">
                <a href="#" onclick="closeTipModal('tipModal')" class="close">
                    &times; </a>
                <div id="tipId" style="text-align: center;"></div>
            </div>
        </div>
    </div>
</div>
<div class="container-fluid">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab"
                                                  data-toggle="tab" id="bookHall">图书大厅</a></li>
        <li role="presentation">
            <a href="#model" aria-controls="model" role="tab" data-toggle="tab">图书模型</a>
        </li>
        <li role="presentation"><a href="#result" aria-controls="result" role="tab"
                                   data-toggle="tab">猜你喜欢</a></li>
    </ul>
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active text-center" id="home"
             style="margin: 30px auto;">
            <div class="form-group text-center">
                <label>图书编号:</label>
                <input type="text" id="bookId" class="form-control" placeholder="按编号搜索"
                       style="display:inline;width: 150px;">
                <labe style="margin-left: 50px;">图书名称:</labe>
                <input type="text" id="name" class="form-control" placeholder="按名称搜索"
                       style="display:inline;width: 150px;">
                <button type="button" id="search" class="btn btn-info"
                        style="margin-right: 50px;">搜索
                </button>
                <button type="button" id="submitRating" class="btn btn-success"
                        style="margin-right: 50px;">提交评分
                </button>
            </div>
            <table class="table">
                <tr>
                    <td></td>
                    <td>图书编号</td>
                    <td>图书名称</td>
                    <td>作者</td>
                    <td>出版社</td>
                    <td>价格</td>
                    <td>对图书打分</td>
                </tr>
                <tbody id="hall">
                </tbody>
            </table>
            <nav aria-label="Page navigation">
                <ul id="page" class="pagination"></ul>
            </nav>
        </div>
        <div role="tabpanel" class="tab-pane text-center" id="model"
             style="width: 80%;margin: 30px auto;">
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="rank" class="col-sm-2 control-label">隐藏因子数:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="rank" placeholder="隐藏因子数">
                    </div>
                </div>
                <div class="form-group">
                    <label for="lambda" class="col-sm-2 control-label">正则化参数:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="lambda" placeholder="正则化参数">
                    </div>
                </div>
                <div class="form-group">
                    <label for="iterator" class="col-sm-2 control-label">迭代次数:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="iterator"
                               placeholder="迭代次数">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-10">
                        <button type="button" id="modelBtn" class="btn btn-primary col-md-6">建立模型
                        </button>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="progressModal" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="progress progress-striped active"
                             style="margin-bottom: 0px; height: 25px; border-radius: 5px;">
                            <div id="progressId" class="progress-bar"
                                 style="width: 1%; height: 100%;">0%
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal fade" id="modelModal" tabindex="-1" role="dialog"
                 aria-labelledby="myModalLabel2">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="alert alert-info" style="margin-bottom: 0px;">
                            <a href="#" onclick="closeModelModal('modelModal')" class="close">
                                &times; </a>
                            <div id="modelId" style="text-align: center"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div role="tabpanel" class="tab-pane" id="result" style="margin: 30px auto;">
            <div class="form-group text-center">
                <label>用户编号:</label>
                <input type="text" id="userId" class="form-control" placeholder="用户编号"
                       style="display:inline;width: 150px;">
                <button type="button" id="userBtn" class="btn btn-info"
                        style="margin-right: 50px;">查询
                </button>
                <labe style="margin-left: 50px;">推荐数目:</labe>
                <select id="number" class="form-control" style="display:inline; width: 150px;">
                    <option>6</option>
                    <option>9</option>
                    <option>12</option>
                </select>
                <button type="button" id="recommendBtn" class="btn btn-success">推荐</button>
            </div>
            <span id="userLabel" style="margin-left: 10%;display: none;">用户基本信息:</span>
            <div style="width: 80%;margin-left: 10%;">
                <table class="table table-bordered" id="userBaseInfo"
                       style="width: 100%;display: none;font-size: 14px; text-align: center">
                </table>
            </div>
            <span id="ratingLabel" style="margin-left: 10%;display: none;">用户评分的书籍有:</span>
            <div style="width: 80%;margin-left: 10%;">
                <table class="table table-bordered" id="ratingBook"
                       style="width: 100%;display: none;font-size: 14px;"></table>
            </div>
            <span id="recommendLabel" style="margin-left: 10%;display:none;">用户可能喜欢的书籍:</span>
            <div style="width: 80%;margin-left: 10%;">
                <table class="table table-bordered" id="recommendBook"
                       style="width: 100%;display: none;font-size: 14px;"></table>
                <div class="modal fade" id="recommendModal" tabindex="-1" role="dialog"
                     aria-labelledby="myModalLabel2" style="position: fixed">
                    <div class="modal-dialog" role="document" style="text-align: center;">
                        <img src="${pageContext.request.contextPath}/static/images/load.gif"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
