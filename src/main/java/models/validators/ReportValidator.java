package models.validators;

import java.util.ArrayList;
import java.util.List;

import actions.views.ReportView;
import constants.MessageConst;

/**
 * 日報インスタンスの設定されている値のバリデーションを行う
 *
 */
public class ReportValidator {

    /**
     * 日報インスタンスの各項目についてバリデーションを行う
     * @param rv  日報インスタンス
     * @return  エラーのリスト
     */
    public static List<String> validate(ReportView rv){

        //AllayList<String>型のインスタンスを生成し、List<String>型のerrors変数に代入
        List<String> errors = new ArrayList<String>();

        //タイトルのチェック
        String titleError = validateTitle(rv.getTitle());
        if(!titleError.equals("")) {
            errors.add(titleError);
        }

        //内容のチェック
        String contentError = validateContent(rv.getContent());
        if(!contentError.equals("")) {
            errors.add(contentError);
        }

        return errors;

    }

    /**
     * タイトルに入力値があるかどうかチェックし、入力値がなければエラーメッセージを返却
     * @param title タイトル
     * @return  エラーメッセージ
     */
    private static String validateTitle(String title) {
        if(title == null || title.equals("")) {
            return MessageConst.E_NOTITLE.getMessage();

        }

        //入力値がある場合は空文字列を返却
        return "";

    }

    /**
     * 内容に入力値があるかチェックし、入力値がなければエラーメッセージを返却
     * @param content   内容
     * @return  エラーメッセージ
     */
    private static String validateContent(String content) {
        if(content == null || content.equals("")) {
            return MessageConst.E_NOCONTENT.getMessage();   //"内容を入力してください。"
        }

        //入力値がある場合は空文字を返却
        return "";
    }

} //End of ReportValidator


