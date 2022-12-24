# TestBankApp  
Тестовое задание для компании ЦФТ  

Реализованы 4 функции  
1. Пользователь вводит BIN банковской карты и видит всю доступную информацию о нём, загруженную с https://binlist.net/  
2. История предыдущих запросов выводится списком  
3. История предыдущих запросов не теряется при перезапуске приложения  
4. Нажатие на URL банка, телефон банка, координаты страны отправляет пользователя в приложение, которое может обработать эти данные (браузер, звонилка, карты)  

Использовал Retrofit для отправки GET запросов и чтения JSON-файлов, OkHttp для создания наглядных логов   
Для сохранения истории запросов использовал сохранениие запросов в базу данных SQLite  
