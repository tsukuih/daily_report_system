package services;

import javax.persistence.EntityManager;

import utils.DBUtil;

/**
 * DB接続に関わる共通処理を行うクラス
 * スーパークラス
 */
public class ServiceBase {

    /**
     * EntityManegerインスタンスの生成
     * データベースへの接続の確立、データベースの操作の実行でできるようになる
     */
    protected EntityManager em = DBUtil.createEntityManager();

    /**
     * EntityManegerのクローズ
     */
    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }

}
