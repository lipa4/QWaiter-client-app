# QWaiter-client-app
QWaiter is project intended for Restaurants and Bars. It's a platform for ordering and paying food and drinks. QWaiter client app is android app for customers and ther is one more for waiters.The one  that is here is functional although it's not finished. The whole project is using Firebase as a backend(BaaS).
Project is currently under experiments and development and will be updated. 
 Here are some screenshots of app:

![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-134247.resized.png?alt=media&token=274e89d6-bd96-4128-82cd-3fd260686e22) ![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-134316.png?alt=media&token=638ce908-e957-4a21-8ead-bec0c0329616) ![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-134332.resized.png?alt=media&token=5051be2f-5564-436c-9cb0-5fa6d36b887a) ![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-135004.resized.png?alt=media&token=893b7e08-0cc0-4def-baea-f96b50ae0296) ![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-135014.resized.png?alt=media&token=5f1eccdb-481f-44a5-baec-b03a339323a1) ![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-135029.resized.png?alt=media&token=517c9295-2fa5-4b70-8166-6d2b69369e2e) ![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-135046.resized.png?alt=media&token=f4fa9126-4d1b-4d00-8966-838567b99000) ![Alt text](https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2FScreenshot_20170827-135118.resized.png?alt=media&token=dcc99502-2a3a-4fea-8f54-32a501ecf41a)


If you want to try, here are some instructions:

I genereted apks for you and here you can get them:

Client app - https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2Fclient.apk?alt=media&token=cce9a13f-99f2-41ff-9109-e08df6861548

Waiter(server) app - https://firebasestorage.googleapis.com/v0/b/qwaiter-f1168.appspot.com/o/QWaiter%2Fserver.apk?alt=media&token=1408dba4-4eee-4584-9079-493bcaa2d600

With server app you don't have much to do for now as it is only to demonstrate.With client app you log and you get screen with two buttons, one with qrcode and one with nfc symbol. These are the ways you can check in to app. Each of them contains id value of the place and a table you are sitting. You have two example qrcodes downhere to test, one is for a bar and one for a restaurant. If you want to try nfc(you need nfc tag) I will leave you text that you should write to nfc tag. You have small app here in repository for writing and reading nfc tags, and scanning qr codes and parts of it are implemented in these apps.

These two are the only that have menu created so for now you can only use them for try.You may find some other but there are blank:

BAR QRCODE:


<a rel='nofollow' href='http://www.qrcode-generator.de' border='0' style='cursor:default'><img src='https://chart.googleapis.com/chart?cht=qr&chl=bar.4.barKey1&chs=180x180&choe=UTF-8&chld=L|2' alt=''></a>

RESTAURANT:


<a rel='nofollow' href='http://www.qrcode-generator.de' border='0' style='cursor:default'></a><img src='https://chart.googleapis.com/chart?cht=qr&chl=res.7.restaurantKey1&chs=180x180&choe=UTF-8&chld=L|2' alt=''>

(On the same table is nfc and qrcode and both of them contain the same value.)

Values for nfc tags that are in qrcode-s;

res.7.restaurantKey1

bar.4.barKey1
