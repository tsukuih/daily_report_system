package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
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




}   // end of EmployeeAction
