package models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import constants.JpaConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 日報データのDTOモデル
 *
 */
@Table(name = JpaConst.TABLE_REP) //TABLE_REP="reports"

@NamedQueries({

    //全ての日報をidの降順に取得する
    @NamedQuery(
            name = JpaConst.Q_REP_GET_ALL,
            query = JpaConst.Q_REP_GET_ALL_DEF),

    //全ての日報の件数を取得する
    @NamedQuery(
            name = JpaConst.Q_REP_COUNT,
            query = JpaConst.Q_REP_COUNT_DEF),

    //指定した従業員が作成した日報をidの降順で全件取得
    @NamedQuery(
            name = JpaConst.Q_REP_GET_ALL_MINE,
            query = JpaConst.Q_REP_GET_ALL_MINE_DEF),

    //指定した従業員が作成した日報の件数を取得する
    @NamedQuery(
            name = JpaConst.Q_REP_COUNT_ALL_MINE,
            query = JpaConst.Q_REP_COUNT_ALL_MINE_DEF)

})

@Getter //全てのクラスフィールドについてgetterを自動生成する（Lombox）
@Setter //全てのクラスフィールドについてsetterを自動生成する（Lombox）
@NoArgsConstructor //引数なしのコンストラクタを自動生成する（Lombox）
@AllArgsConstructor //全てのクラスフィールドを引数にもつ引数ありコンストラクタを自動生成する（Lombox）

@Entity
public class Report {

    /**
     * id
     */
    @Id
    @Column(name = JpaConst.REP_COL_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 日報を登録した従業員
     * 
     */
    @ManyToOne
    @JoinColumn(name = JpaConst.REP_COL_EMP, nullable = false)
    private Employee employee;  //ログインしてる従業員の情報をオブジェクトのままemployeeフィールドに格納

    /**
     * いつの日報かを示す日付
     * LocalDate：年月日のみ管理
     */
    @Column(name = JpaConst.REP_COL_REP_DATE)
    private LocalDate reportDate;

    /**
     * 日報のタイトル
     */
    @Column(name = JpaConst.REP_COL_TITLE, length = 255, nullable = false)
    private String title;

    /**
     * 日報の内容
     */
    @Lob //テキストエリアの指定（改行もデータベースに保存される）
    @Column(name = JpaConst.REP_COL_CONTENT, nullable = false)
    private String content;

    /**
     * 登録日時
     * LocalDateTime：何月日＋時分秒を管理
     */
    @Column(name = JpaConst.REP_COL_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新日時
     * LocalDateTime：何月日＋時分秒を管理
     */
    @Column(name = JpaConst.REP_COL_UPDATED_AT, nullable = false)
    private LocalDateTime updatedAt;

}
