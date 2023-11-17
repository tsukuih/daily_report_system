package actions.views;

import java.util.ArrayList;
import java.util.List;

import models.Report;

/**
 * 日報データの DTOモデル ⇔ Viewモデル の変換を行うクラス
 *
 */
public class ReportConverter {

    /**
     * ViewモデルのインスタンスからDTOモデルのインスタンスを作成
     * @param rv ReportViewのインスタンス
     * @return Reportのインスタンス（DTOモデルのインスタンス）
     */
    public static Report toModel(ReportView rv) {

        return new Report(
                rv.getId(),
                EmployeeConverter.toModel(rv.getEmployee()),
                rv.getReportDate(),
                rv.getTitle(),
                rv.getContent(),
                rv.getCreatedAt(),
                rv.getUpdatedAt()
                );

    }

    /**
     * DTOモデルのインスタンスからViewモデルのインスタンスを作成
     * @param r Reportのインスタンス（DTOモデルのインスタンス）
     * @return ReportViewのインスタンス
     */
    public static ReportView toView(Report r) {

        if(r == null) {
            //DTOモデルにデータが無い場合
            return null;
        }

        //DTOモデルにデータがある場合
        return new ReportView(
                r.getId(),
                EmployeeConverter.toView(r.getEmployee()),
                r.getReportDate(),
                r.getTitle(),
                r.getContent(),
                r.getCreatedAt(),
                r.getUpdatedAt()
                );

    }

    /**
     * DTOモデルのリストからViewモデルのリストを作成する
     * @param list DTOモデル
     * @return Viewモデルのリスト
     */
    public static List<ReportView> toViewList(List<Report> list){
        List<ReportView> evs = new ArrayList<>();

        for (Report r : list) {
            evs.add(toView(r));
        }

        return evs;

    }

    /**
     * Viewモデルの全内容をDTOモデルのフィールドにコピー
     * @param r  DTOモデル（コピー先）
     * @param rv Viewモデル（コピー元）
     */
    public static void copyViewToModel(Report r, ReportView rv) {
        
    }




}   // End of ReportConverter
