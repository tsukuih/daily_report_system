package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.PropertyConst;
import services.EmployeeService;

/**
 * 従業員に関わる処理を行うActionクラス
 */
public class EmployeeAction extends ActionBase {

    // EmployeeService型の変数serviceを定義
    private EmployeeService service;

    /**
     * メソッドを実行する
     */
    @Override
    // フロントコントローラから直接呼び出されるメソッド
    // テーブル操作で必要となるEmployeeServiceのインスタンスを作成し、スーパークラスであるActionBaseのinvoke()メソッドを呼び出す
    public void process() throws ServletException, IOException {

        // EmployeeServiceのインスタンスを作成し、service変数に代入
        // 従業員に関する処理を行う準備を行う
        service = new EmployeeService();

        // パラメータ command の値に該当するメソッドを実行
        // ActionBaseクラスのメソッド
        invoke();

        // パラメータcommandの値が取得が完了したので
        // EmployeeServiceクラスのインスタンスを破棄する（メモリから解放）
        service.close();

    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException {

        // 指定されたページ数の一覧画面に表示するデータを取得
        // 指定されたページ番号の従業員情報を取得し、List<EmployeeView>型のリスト変数employeesに格納する
        // リスト変数には指定した従業員情報のリストが格納される
        int page = getPage();
        List<EmployeeView> employees =  service.getPerPage(page);

        // 全ての従業員データの件数を取得
        long employeeCount = service.countAll();

        // リクエストスコープにパラメータを設定
        putRequestScope(AttributeConst.EMPLOYEES, employees);   //取得した従業員データ
        putRequestScope(AttributeConst.EMP_COUNT, employeeCount);   //全ての従業員データ件数
        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //１ページに表示するレコードの数

        // セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        // 一覧画面を表示
        forward(ForwardConst.FW_EMP_INDEX);

    }

    /**
     * 新規登録画面を表示する
     * @throws ServletException;
     * @throws IOException;
     */
    public void entryNew() throws ServletException, IOException{

        putRequestScope(AttributeConst.TOKEN, getTokenId());    //CSRF対策用トークン
        putRequestScope(AttributeConst.EMPLOYEE, new EmployeeView());   //空の従業員インスタンス

        //新規登録画面を表示
        forward(ForwardConst.FW_EMP_NEW);

    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //パラメータの値を元に従業員情報のインスタンスを作成する
            EmployeeView ev = new EmployeeView(
                    null,
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

            //アプリケーションスコープからpepper文字列を取得
            String pepper = getContextScope(PropertyConst.PEPPER);

            //従業員情報登録
            List<String> errors = service.create(ev, pepper);

            if (errors.size() > 0) {
                //登録中にエラーがあった場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.EMPLOYEE, ev); //入力された従業員情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_EMP_NEW);

            } else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }

        }
    }

    /**
     * 詳細画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException {

        //idを条件に従業員データを取得する
        EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

        if(ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            // データが取得できなかった、または論理削除されている場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }

        //取得した従業員情報をリクエストパラメータに設定
        putRequestScope(AttributeConst.EMPLOYEE, ev);

        //詳細画面を表示（show.jspを呼び出す）
        forward(ForwardConst.FW_EMP_SHOW);

    }

    /**
     * 編集画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws  ServletException, IOException{

        //idを条件に従業員データを取得する
        EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

        if(ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            // データを取得できなかった、または論理削除されている場合はエラー画面を表示
            forward(ForwardConst.FW_ERR_UNKNOWN);
            return;
        }

        //CSRF対策用トークン
        putRequestScope(AttributeConst.TOKEN, getTokenId());
        //所得した従業員情報
        putRequestScope(AttributeConst.EMPLOYEE, ev);

        //編集画面を表示する
        forward(ForwardConst.FW_EMP_EDIT);

    }

}   // end of EmployeeAction
