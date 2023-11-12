package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.AttributeConst;
import constants.ForwardConst;
import services.EmployeeService;

/**
 * 認証に関する処理を行うActionクラス
 *
 */
public class AuthAction extends ActionBase{

    //フィールドの定義
    private EmployeeService service;


    @Override
    public void process() throws ServletException, IOException {

        //テーブル操作に関するインスタンスを生成し、EmployeeService型変数serviceに格納
        service = new EmployeeService();

        //パラメータのcommandの値に該当するメソッドを実行
        invoke();

        service.close();

    }

    /**
     * ログイン画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void showLogin() throws ServletException, IOException {

        //CSRF対策用トークンを設定
        putSessionScope(AttributeConst.TOKEN, getTokenId());

        //セションにフラッシュメッセージが登録されている場合はリクエストスコープに設定する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //ログイン画面の表示
        forward(ForwardConst.FW_LOGIN);

    }

}