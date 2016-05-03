/**
 * 项目名称：
 * 包名：  com.cnlaunch.golo3.db 
 * 文件名：  BaseDao.java  
 * 功能：  
 * 2014深圳市元征科技股份有限公司-版权所有
 * NO.    Date                      Author     Summary 
 * 001   2014年11月14日-下午7:20:04   韦子龙 
 *
 */
package ikabi.com.mobilesafe.dao;

import android.content.ContentValues;
import android.database.Cursor;

import dao.AbstractDao;
import dao.AbstractDaoSession;
import dao.internal.DaoConfig;

/**
 * 
 * 类名称:BaseDao 
 * 类描述:数据访问对象抽象类，用于实体的持久化和查询、更新。对于每一个实体必须继承该抽象类实现对应Dao 
 * 创建人: 韦子龙 
 * 修改人:
 * 修改人:  
 * 2014年11月14日 下午7:20:04
 * 修改备注:
 * @version 1.0.0
 * 
 */
public abstract class BaseDao<T,K> extends AbstractDao<T, K>{
	private final static Object objLock = new Object();
	
	public BaseDao(DaoConfig config, AbstractDaoSession daoSession) {
		super(config, daoSession);
		// TODO Auto-generated constructor stub
	}

	public BaseDao(DaoConfig config) {
		super(config);
		// TODO Auto-generated constructor stub
		
	}

	/**
	 *  通过原生的SQL语句 更新数据库
	 * @param table 表名
	 * @param values 更新的值
	 * @param whereClause 条件语句
	 * @param whereArgs 条件参数值
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
		int count = -1;
		if (db.isDbLockedByCurrentThread()) {
            synchronized (objLock) {
            	count = db.update(table, values, whereClause, whereArgs);
            }
        } else {
            // Do TX to acquire a connection before locking the objLock to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (objLock) {
                	count = db.update(table, values, whereClause, whereArgs);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
		return count;
	}
	
	/**
	 * 通过原生的SQL语句 更新数据库
	 * @param table 表名
	 * @param values 更新的值
	 * @param whereClause 条件语句
	 * @param whereArgs 条件参数
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm){
		int count = 0;
		if (db.isDbLockedByCurrentThread()) {
            synchronized (objLock) {
            	count = db.updateWithOnConflict(table, values, whereClause, whereArgs, conflictAlgorithm);
            }
        } else {
            // Do TX to acquire a connection before locking the objLock to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (objLock) {
                	count = db.updateWithOnConflict(table, values, whereClause, whereArgs, conflictAlgorithm);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
		return count;
	}
	
	
	/**
	 * 根据条件删除车辆档案 通过原生的SQL
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return 
	 *int
	 * @exception 
	 * @since  1.0.0
	 */
	public int delete(String table, String whereClause, String[] whereArgs){
		int count = 0;
		if (db.isDbLockedByCurrentThread()) {
            synchronized (objLock) {
            	count = db.delete(table, whereClause, whereArgs);
            }
        } else {
            // Do TX to acquire a connection before locking the objLock to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (objLock) {
                	count = db.delete(table, whereClause, whereArgs);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
		return count;
	} 	
	
	/**
	 * 根据条件删除车辆档案 通过原生的SQL
	 * @param sql
	 * @param bindArgs 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
	public void delete(String sql, String[] bindArgs){
	
		if (db.isDbLockedByCurrentThread()) {
            synchronized (objLock) {
            	db.execSQL(sql, bindArgs);
            }
        } else {
            // Do TX to acquire a connection before locking the objLock to avoid deadlocks
            db.beginTransaction();
            try {
                synchronized (objLock) {
                	db.execSQL(sql, bindArgs);
                }
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
	} 	
	/**
	 * 通过原生的SQL语句 查询数据库
	 * @param sql
	 * @param selectionArgs
	 * @return
	 */
	public Cursor query(String sql, String[] selectionArgs){
		Cursor cursor = null;
//		if (db.isDbLockedByCurrentThread()) {
//            synchronized (objLock) {
//            	cursor = db.rawQuery(sql, selectionArgs);
//        	}
//        } else {
//            // Do TX to acquire a connection before locking the objLock to avoid deadlocks
//            db.beginTransaction();
//            try {
//                synchronized (objLock) {
                	cursor = db.rawQuery(sql, selectionArgs);
//                }
//                db.setTransactionSuccessful();
//            } finally {
//                db.endTransaction();
//            }
//        }
		return cursor;
	}
	/**
	 * 支持多表查询的特殊sql查询，单表查询不必使用此方法，调用结束记得关闭游标，业务调用此方法处应加锁
	 * @param sql
	 * @param bindArgs
	 * @return
	 */
	public Cursor sqlQuery(String sql, String[] bindArgs){
		return db.rawQuery(sql, bindArgs);
	}
	/**
	 * Query the given table, returning a Cursor over the result set.
	 * @Title: sqlQuery   
	 * @Description: TODO(这里用一句话描述这个方法的作用)  
	 * @param: @param table
	 * @param: @param columns
	 * @param: @param selection
	 * @param: @param selectionArgs
	 * @param: @param groupBy
	 * @param: @param having
	 * @param: @param orderBy
	 * @param: @return      
	 * @return: Cursor     
	 * @throws
	 */
	public Cursor sqlQuery(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
}
