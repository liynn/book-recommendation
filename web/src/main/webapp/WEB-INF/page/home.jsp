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
    <script src="${pageContext.request.contextPath}/static/js/book.js"
            type="text/javascript"></script>

</head>
<body style="padding: 0;margin: 0;border: 0">
<nav class="navbar navbar-default"
     style="background-color: #3678ac;border-color: #105e89;height: 66px;">
    <div class="container-fluid" style="height: 65px;text-align: center">
        <div class="navbar-header">
            <a class="navbar-brand" href="#"
               style="color: #ecf0f1;padding-left: 100px;font-size: 24px;line-height: 32px;">图书推荐系统</a>
        </div>
    </div>
</nav>
<div class="container-fluid">
    <ul class="nav nav-tabs" role="tablist">
        <li role="presentation" class="active"><a href="#home" aria-controls="home" role="tab"
                                                  data-toggle="tab">介绍</a></li>
        <li role="presentation">
            <a href="#model" aria-controls="model" role="tab" data-toggle="tab">图书模型</a>
        </li>
        <li role="presentation"><a href="#result" aria-controls="result" role="tab"
                                   data-toggle="tab">推荐信息</a></li>
    </ul>
    <div class="tab-content">
        <div role="tabpanel" class="tab-pane active text-center" id="home">简介
        </div>
        <div role="tabpanel" class="tab-pane text-center" id="model"
             style="width: 80%;margin: 30px auto;">
            <div class="form-horizontal">
                <div class="form-group">
                    <label for="address" class="col-sm-2 control-label">评分文件地址:</label>
                    <div class="col-sm-10">
                        <input type="text" class="form-control" id="address"
                               placeholder="评分文件地址">
                    </div>
                </div>
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
