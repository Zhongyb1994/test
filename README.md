# check-order-system
日千万级别订单对账系统
搭建系统的目的：  
1.梳理之前做过的业务  
2.分享给需要的人  
3.欢迎一起探讨
# 业务背景
        业务是移动支付行业，我们日常收单然后请求微信或者支付宝进行底层支付，
    所有商户收到的钱都会到平台方的账户，D1日平台方会将服务商的钱给到服
    务商，并附上服务商的所有订单，服务商拿到钱扣除手续费将钱清分给商户，
    为了保证平台方给我们的钱准确无误，我们需要对账，对账的目的是首先
    确认订单无差异，然后核总账，最后给商户出款。

