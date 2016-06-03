//存放主要交互逻辑js代码
// 模块化

var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now:function () {
            return '/seckill/time/now';
        },
        exposer:function (seckillId) {
            return '/seckill/' + seckillId + '/exposer';
        },
        execution: function (seckillId, md5) {
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }

    },
    //验证手机号
    validatPhone: function (phone) {
        if(phone && phone.length == 11 && !isNaN(phone))
        {
            return true;
        }
        return false;
    },
    countDown:function (seckillId, nowTime, startTime, endTime) {
        var seckillBox = $('#seckill-box');
        if(nowTime < startTime){
            //秒杀未开始，计时时间绑定
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('秒杀倒计时： %D天 %H时 %M分 %S秒');
                seckillBox.html(format);
                //时间完成后回调时间
            }).on('finish.countdown', function () {
                //获取秒杀地址，控制实现逻辑
                seckill.handleSecKill(seckillId, seckillBox);
            });
        }
        else if(nowTime > endTime){
            seckillBox.html("秒杀结束!");
        }
        else
        {
            seckill.handleSecKill(seckillId, seckillBox);
        }
    },
    //处理秒杀逻辑,执行秒杀
    handleSecKill:function (seckillId, node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function (result) {
           //在回调函数中，执行交互流程
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5']
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log("killUrl:" + killUrl);
                    $('#killBtn').one('click',function () {
                        //执行秒杀请求
                        //1 禁用按钮
                        $(this).addClass('disable');
                        //2 发送秒杀请求
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        });
                    });
                    node.show();
                }
                else{
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新计算计时逻辑
                    seckill.countDown(seckillId, now, start, end);
                }
            }
            else{
                console.log("result= "+ result);//TODO
            }

        });
    },
    //详情页秒杀逻辑
    detail:{
        //详情页初始化
        init: function(params){
            //手机验证和登录，计时交互
            //规划我们的交互流程
            var killPhone = $.cookie('killPhone');

            //验证手机号

            if(!seckill.validatPhone(killPhone)){
                //绑定手机号
                var killPhoneModal = $('#killPhoneModal');
                //显示弹出层
                killPhoneModal.modal({
                    show:true,//显示弹出层
                    backdrop:'static',
                    keyboard:false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if(seckill.validatPhone(inputPhone)){
                        //电话写入cookie
                        $.cookie('killPhone', inputPhone, {expires:7,path:'/seckill'});
                        //刷新页面
                        window.location.reload();
                    }
                    else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });
            }
            //已经登录
            //计时交互
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(), {}, function (result) {
               if(result && result['success']){
                   var nowTime = result['data'];
                   seckill.countDown(seckillId, nowTime, startTime, endTime);
               }
               else{
                   console.log('result:' + result);//TODO
               }
            });
        }
    }
}