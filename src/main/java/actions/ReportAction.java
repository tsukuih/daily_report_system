package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ReportService;

/**
 * 日報に関する処理を行うActionクラス
 *
 */
public class ReportAction extends ActionBase{

    //変数の定義（ReportService型のservice変数）
    private ReportService service;

    /**
     * メソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        //ReportServiceクラスのインスタンスを生成し、その参照値をservice変数に代入する
        //service変数から生成したReportServiceクラスのインスタンスのメソッド等にアクセスすることができる
        service = new ReportService();

        //メソッドを実行
        invoke();
        service.close();

    }

    /**
     * 一覧画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void index() throws ServletException, IOException{

        //指定されたページ数の一覧画面に表示する日報データを取得
        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);

        //全日報データの件数を取得
        Long reportsCount = service.countAll();

        putRequestScope(AttributeConst.REPORTS, reports);   //取得した日報データ

        putRequestScope(AttributeConst.REP_COUNT, reportsCount); //全ての日報データの件数

        putRequestScope(AttributeConst.PAGE, page); //ページ数
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE); //1ページに表示するレコードの数

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if(flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        //一覧画面を表示
        forward(ForwardConst.FW_REP_INDEX); //index.jspの呼び出し
    }


    /**
     * 新規登録画面を表示する
     * @throws ServletException
     * @throws IOException
     */
    public void entryNew() throws ServletException,IOException {

        //リクエストスコープにユーザの承認に使用するトークンIDを設定
        //CSRF対策用トークン
        putRequestScope(AttributeConst.TOKEN, getTokenId());

        //日報情報の空インスタンスに、日報の日付＝今日の日付を設定する
        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());  //reportDateプロパティに今日の日付を設定
        //日付のみ設定済みの日報インスタンスをリクエストスコープに設定
        putRequestScope(AttributeConst.REPORT, rv);

        //新規登録画面を表示（new.jspの呼び出しを行う）
        forward(ForwardConst.FW_REP_NEW);

    }

    /**
     * 新規登録を行う
     * @throws ServletException
     * @throws IOException
     */
    public void create() throws ServletException, IOException{

        //CSRF対策  tokenのチエック
        if(checkToken()) {

            //日報の日付が入力されていなければ、今日の日付を設定
            LocalDate day = null;
            if(getRequestParam(AttributeConst.REP_DATE) == null
                    || getRequestParam(AttributeConst.REP_DATE).equals("")) {
                day = LocalDate.now();

            }else {
                day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));
            }

            //セッションスコープからログイン中の従業員情報を取得
            //LOGIN_EMP：login_employee
            //evにはログイン中の従業員のオブジェクトが格納されている
            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            //パラメータの値を元に日報情報のインスタンスを作成する
            ReportView rv = new ReportView(
                null,
                ev, //ログインしている従業員を、日報作成者として登録する
                day,
                getRequestParam(AttributeConst.REP_TITLE),  //REP_TITLE：title
                getRequestParam(AttributeConst.REP_CONTENT), //REP_CONTENT：content_msg
                null,
                null);

            //日報情報を登録
            List<String> errors = service.create(rv);

            if(errors.size() > 0) {
                //登録中にエラーがあった場合
                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); //入力さらた日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //新規登録画面を再表示
                forward(ForwardConst.FW_REP_NEW); //reports/new

            }else {
                //登録中にエラーがなかった場合

                //セッションに登録完了のフラッシュメッセージを設定
                //I_REGISTERED："登録が完了しました。"
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }

        }

    }

    /**
     * 詳細画面を表示する：show()
     * @throws ServletException
     * @throws IOException
     */
    public void show() throws ServletException, IOException{

        //idを条件に日報データを取得する
        //リクエストパラメータからレポートIDを取得し、そのレポートIDを元にデータベースからそのレポート情報を取得する
        //ReportView型のrv変数は、取得したReportViewのインスタンスを参照している
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        if(rv == null) {

            //該当の日報データが存在しない場合はエラー画面を表示
            //jspの呼び出し error/unknown：お探しのページは見つかりませんでした。
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            //取得した日報データをリクエストスコープに設定する
            //属性名：report、値：rv（ReportView）
            putRequestScope(AttributeConst.REPORT, rv);

            //詳細画面を表示
            //jspの呼び出し  reports/show
            forward(ForwardConst.FW_REP_SHOW);
        }

    }


    /**
     * 編集画面を表示する；edit()
     * @throws ServletException
     * @throws IOException
     */
    public void edit() throws ServletException, IOException{

        //idを条件に日報データを取得する
        //リクエストパラメータからidの値を取得し、テーブルから該当する日報データを取得し、rv変数に代入する
        //rvには、該当するidの従業員が登録した日報リストの参照値が格納されている
        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        //セッションスコープからログイン中の従業員情報を取得
        //LOGIN_EMP：loginEmployee
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null || ev.getId() != rv.getEmployee().getId()) {

            //該当の日報データが存在しない、または、
            //ログインしている従業員が日報の作成者ではない場合はエラー画面を表示
            //FW_ERR_UNKNOWN：error/unknown 「お探しのページは見つかりませんでした。」
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            //CSRF対策用トークン
            putRequestScope(AttributeConst.TOKEN, getTokenId());

            //取得した日報データ
            putRequestScope(AttributeConst.REPORT, rv);

            //編集画面を表示
            //FW_REP_EDIT："reports/edit"
            forward(ForwardConst.FW_REP_EDIT);

        }
    }

    /**
     * 更新を行う
     * @throws ServletException
     * @throws IOException
     */
    public void update() throws ServletException, IOException {

        //CSRF対策 tokenのチェック
        if (checkToken()) {

            //idを条件に日報データを取得する
            ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            //入力された日報内容を設定する
            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            //日報データを更新する
            List<String> errors = service.update(rv);

            if (errors.size() > 0) {
                //更新中にエラーが発生した場合

                putRequestScope(AttributeConst.TOKEN, getTokenId()); //CSRF対策用トークン
                putRequestScope(AttributeConst.REPORT, rv); //入力された日報情報
                putRequestScope(AttributeConst.ERR, errors); //エラーのリスト

                //編集画面を再表示
                forward(ForwardConst.FW_REP_EDIT);
            } else {
                //更新中にエラーがなかった場合

                //セッションに更新完了のフラッシュメッセージを設定
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                //一覧画面にリダイレクト
                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

            }
        }
    }

}   // End of ReportAction

