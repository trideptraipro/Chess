# Chess

Chào, Tôi là Trí sau đây là cách cài đặt App:

B1: Clone code về máy
Nếu chạy như 1 client
B2.1.1: Chạy file code MangMayTinh.Chess.Connection.Client.Main
B2.1.2: Click vào "Create a new account" sau đó đăng kí 1 tài khoản
B2.1.3: Đăng nhập tài khoản đã đăng kí.
B2.1.4: Click "New game" để vào game

Nếu chạy như 1 Server, máy bạn phải có MySQL, sửa kết nối trong MangMayTinh.Chess.Model.DAO.SQLConect dòng 26
B2.2.1: Chạy file code MangMayTinh.Chess.Connection.Server.Server và MangMayTinh.Chess.Connection.Server.ServerInfo
B2.2.2: sửa code file MangMayTinh.Chess.View.Login dòng 78 và MangMayTinh.Chess.Connection.Client.Client dòng 376 thành ip của máy tính chạy server 
hoặc nếu chạy trên cùng máy có thể chuyển thành localhost
B2.2.3: Tiến hành như bước 2.1.1
