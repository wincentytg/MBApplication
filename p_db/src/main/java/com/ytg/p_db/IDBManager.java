package com.ytg.p_db;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;
/**
 *
 * @author 于堂刚
 */
public interface IDBManager {
	/**
	 * 获取数据库
	 * 
	 * @return SQLiteDatabase对象
	 */
	public abstract SQLiteDatabase getDataBase();

	/**
	 * 表是否存在
	 * 
	 * @param tableName
	 *            表名称
	 * @return 是否存在
	 * @throws DBException
	 */
	public abstract boolean isTableExist(String tableName) throws DBException;

	/**
	 * 插入实体类(默认主键自增)
	 * 
	 * @param entity
	 *            映射实体
	 * @return 插入成功后数据的行号
	 * @throws DBException
	 */
	public abstract long insert(Object entity) throws DBException;

	/**
	 * 插入实体类
	 * 
	 * @param entity
	 *            映射实体
	 * @param flag
	 *            flag为true是自动生成主键,flag为false时使用实体中的@id注解的属性值为主键值.
	 * @return 插入成功后数据的行号
	 * @throws DBException
	 */
	public abstract long insert(Object entity, boolean flag) throws DBException;

	/**
	 * 插入实体类列表(默认主键自增)
	 * 
	 * @param entityList
	 *            映射实体列表
	 * @return 插入成功后数据的行号和
	 * @throws DBException
	 */
	public abstract long insertList(List<?> entityList) throws DBException;

	/**
	 * 插入实体类列表
	 * 
	 * @param entityList
	 *            映射实体列表
	 * @param flag
	 *            flag为true是自动生成主键,flag为false时使用实体中的@id注解的属性值为主键值.
	 * @return 插入成功后数据的行号和
	 * @throws DBException
	 */
	public abstract long insertList(List<?> entityList, boolean flag)
			throws DBException;

	/**
	 * 插入或更新实体类(根据ID进行查询更新)
	 * 
	 * @param entity
	 *            映射实体类
	 *            ID
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdateById(Object entity) throws DBException;

	/**
	 * 插入或更新实体类(根据ID进行查询更新)
	 * 
	 * @param entity
	 *            映射实体类
	 *            ID
	 * @param flag
	 *            flag为true是自动生成主键,flag为false时使用实体中的@id注解的属性值为主键值.
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdateById(Object entity, boolean flag)
			throws DBException;

	/**
	 * 插入或更新实体类(根据条件进行查询更新)
	 * 
	 * @param entity
	 *            映射实体类
	 * @param whereClause
	 *            where语句
	 * @param whereArgs
	 *            where参数
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdate(Object entity, String whereClause,
                                        String[] whereArgs) throws DBException;

	/**
	 * 插入或更新实体类(根据条件进行查询更新)
	 * 
	 * @param entity
	 *            映射实体类
	 * @param flag
	 *            flag为true是自动生成主键,flag为false时使用实体中的@id注解的属性值为主键值.
	 * @param whereClause
	 *            where语句
	 * @param whereArgs
	 *            where参数
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdate(Object entity, boolean flag,
                                        String whereClause, String[] whereArgs) throws DBException;

	/**
	 * 插入或更新实体类集合(根据ID集合进行查询更新)
	 * 
	 * @param entityList
	 *            映射实体类集合
	 *            ID集合
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdateListById(List<?> entityList)
			throws DBException;

	/**
	 * 插入或更新实体类集合(根据ID集合进行查询更新)
	 * 
	 * @param entityList
	 *            映射实体类集合
	 *            ID集合
	 * @param flag
	 *            flag为true是自动生成主键,flag为false时使用实体中的@id注解的属性值为主键值.
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdateListById(List<?> entityList, boolean flag)
			throws DBException;

	/**
	 * 插入或更新实体类集合(根据条件进行查询更新)
	 * 
	 * @param entityList
	 *            映射实体类集合
	 * @param whereClause
	 *            where语句
	 * @param whereArgs
	 *            where参数集合
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdateList(List<?> entityList,
                                            String whereClause, List<String[]> whereArgs) throws DBException;

	/**
	 * 插入或更新实体类集合(根据条件进行查询更新)
	 * 
	 * @param entityList
	 *            映射实体类集合
	 * @param flag
	 *            flag为true是自动生成主键,flag为false时使用实体中的@id注解的属性值为主键值.
	 * @param whereClause
	 *            where语句
	 * @param whereArgs
	 *            where参数集合
	 * @return 插入或修改后数据的行号
	 * @throws DBException
	 */
	public abstract long insertOrUpdateList(List<?> entityList, boolean flag,
                                            String whereClause, List<String[]> whereArgs) throws DBException;

	/**
	 * 删除数据(根据ID进行删除)
	 * 
	 * @param entity
	 *            映射实体类
	 * @throws DBException
	 */
	public abstract void deleteById(Object entity) throws DBException;

	/**
	 * 删除数据集合(根据ID进行删除)
	 * 
	 *            映射实体类
	 * @throws DBException
	 */
	public abstract void deleteListById(List<?> entityList) throws DBException;

	/**
	 * 删除数据(根据条件进行删除)
	 * 
	 * @param clsType
	 *            映射类类型
	 * @param whereClause
	 *            where语句的sql
	 * @param whereArgs
	 *            where语句的sql的绑定变量的参数
	 * @throws DBException
	 */
	public abstract void delete(Class<?> clsType, String whereClause,
                                String[] whereArgs) throws DBException;

	/**
	 * 删除全部数据
	 * 
	 * @param clsType
	 *            映射类类型
	 * @throws DBException
	 */
	public abstract void deleteAll(Class<?> clsType) throws DBException;

	/**
	 * 删除全部数据
	 * 
	 * @param clsTypes
	 *            映射类类型集合
	 * @throws DBException
	 */
	public abstract void deleteAllList(List<Class<?>> clsTypes)
			throws DBException;

	/**
	 * 更新数据(根据ID进行更新)
	 * 
	 * @param entity
	 *            映射实体类
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long updateById(Object entity) throws DBException;

	/**
	 * 更新数据(根据ID进行更新)
	 * 
	 * @param entity
	 *            映射实体类
	 * @param columns
	 *            需要更新的字段名
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long updateById(Object entity, String... columns)
			throws DBException;

	/**
	 * 更新数据(根据ID进行更新)
	 * 
	 * @param entity
	 *            映射实体类
	 * @param whereClause
	 *            where语句的sql
	 * @param whereArgs
	 *            where语句的sql的绑定变量的参数
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long update(Object entity, String whereClause,
                                String[] whereArgs) throws DBException;

	/**
	 * 更新数据(根据ID进行更新)
	 * 
	 * @param entity
	 *            映射实体类
	 * @param whereClause
	 *            where语句的sql
	 * @param whereArgs
	 *            where语句的sql的绑定变量的参数
	 * @param columns
	 *            需要更新的字段名
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long update(Object entity, String whereClause,
                                String[] whereArgs, String... columns) throws DBException;

	/**
	 * 更新数据集合(根据ID进行更新)
	 * 
	 * @param entityList
	 *            映射实体类集合
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long updateListById(List<?> entityList) throws DBException;

	/**
	 * 更新数据集合(根据ID进行更新)
	 * 
	 *            映射实体类
	 * @param columns
	 *            需要更新的字段名
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long updateListById(List<?> entityList,
                                        List<String[]> columns) throws DBException;

	/**
	 * 更新数据集合(根据条件进行更新)
	 * 
	 * @param entityList
	 *            映射实体类集合
	 * @param whereClause
	 *            where语句的sql
	 * @param whereArgs
	 *            where语句的sql的绑定变量的参数
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long updateList(List<?> entityList,
                                    List<String> whereClauses, List<String[]> whereArgs)
			throws DBException;

	/**
	 * 更新数据集合(根据条件进行更新)
	 * 
	 * @param entityList
	 *            映射实体类集合
	 * @param whereClause
	 *            where语句的sql
	 * @param whereArgs
	 *            where语句的sql的绑定变量的参数
	 * @param columns
	 *            需要更新的字段名
	 * @return 修改成功后的数据行号
	 * @throws DBException
	 */
	public abstract long updateList(List<?> entityList,
                                    List<String> whereClauses, List<String[]> whereArgs,
                                    List<String[]> columns) throws DBException;

	/**
	 * 根据查询一条数据(根据ID进行查询)
	 * 
	 * @param clsType
	 *            映射类类型
	 * @id ID
	 * @return 映射的实体
	 * @throws DBException
	 */
	public abstract <T> T queryOneById(Class<T> clsType, int id)
			throws DBException;

	/**
	 * 根据查询一条数据(根据条件进行查询)
	 * 
	 * @param clsType
	 *            映射类类型
	 * @param selection
	 *            where语句的sql
	 * @param selectionArgs
	 *            where语句的sql的绑定变量的参数
	 * @return 映射的实体
	 * @throws DBException
	 */
	public abstract <T> T queryOne(Class<T> clsType, String selection,
                                   String[] selectionArgs) throws DBException;

	/**
	 * 执行查询语句
	 * 
	 * @param clsType
	 *            映射类类型
	 * @param sql
	 *            sql语句
	 * @param selectionArgs
	 *            绑定变量的参数值
	 * @return 映射实体列表
	 * @throws DBException
	 */
	public abstract <T> List<T> rawQuery(Class<T> clsType, String sql,
                                         String[] selectionArgs) throws DBException;

	/**
	 * 查询映射实体列表
	 * 
	 * @param clsType
	 *            映射类类型
	 * @return 映射实体列表
	 * @throws DBException
	 */
	public abstract <T> List<T> queryList(Class<T> clsType) throws DBException;

	/**
	 * 查询映射实体列表
	 * 
	 * @param clsType
	 *            映射类类型
	 * @param selection
	 *            where语句的sql
	 * @param selectionArgs
	 *            where语句的sql的绑定变量的参数
	 * @return 映射实体列表
	 * 
	 * @throws DBException
	 */
	public abstract <T> List<T> queryList(Class<T> clsType, String selection,
                                          String[] selectionArgs) throws DBException;

	/**
	 * 查询映射实体列表
	 * 
	 * @param clsType
	 *            映射类类型
	 * @param columns
	 *            查询的列
	 * @param selection
	 *            where语句的sql
	 * @param selectionArgs
	 *            where语句的sql的绑定变量的参数
	 * @param groupBy
	 *            分组语句
	 * @param having
	 *            分组后的过滤语句
	 * @param orderBy
	 *            排序
	 * @param limit
	 *            limit语句
	 * @return 映射实体列表
	 * @throws DBException
	 */
	public abstract <T> List<T> queryList(Class<T> clsType, String[] columns,
                                          String selection, String[] selectionArgs, String groupBy,
                                          String having, String orderBy, String limit) throws DBException;

	/**
	 * 检查是否存在数据(根据映射类表查询)
	 * 
	 * @param clsType
	 *            映射类类型
	 * @return 如果存在返回true, 不存在为false
	 * @throws DBException
	 */
	public abstract boolean isExist(Class<?> clsType) throws DBException;

	/**
	 * 检查是否存在数据
	 * 
	 * @param sql
	 *            sql语句
	 * @param selectionArgs
	 *            绑定变量的参数值
	 * @return 如果存在返回true, 不存在为false
	 * @throws DBException
	 */
	public abstract boolean isExist(String sql, String[] selectionArgs)
			throws DBException;

	/**
	 * 返回一个查询的结果条数
	 * 
	 * @param clsType
	 *            映射类类型
	 * @return 总条数.
	 */
	public int queryCount(Class<?> clsType) throws DBException;

	/**
	 * 返回一个查询的结果条数
	 * 
	 * @param sql
	 *            查询sql
	 * @param selectionArgs
	 *            绑定变量的参数值
	 * @return 总条数.
	 * @throws DBException
	 */
	public int queryCount(String sql, String[] selectionArgs)
			throws DBException;

	/**
	 * 封装执行sql代码.
	 * 
	 * @param sql
	 *            sql语句
	 * @param selectionArgs
	 *            绑定变量的参数值
	 * @throws DBException
	 */
	public void execSql(String sql, Object[] selectionArgs) throws DBException;

	/**
	 * 创建表
	 * 
	 * @param cls
	 *            映射类类型
	 * @throws DBException
	 */
	public <T> void createTable(Class<T> cls) throws DBException;

	/**
	 * 通过映射创建多个表
	 * 
	 * @param classes
	 *            映射类类型集合
	 */
	public <T> void createTables(Class<T>[] classes) throws DBException;

	/**
	 * 通过映射删除表
	 * 
	 * @param cls
	 *            映射类类型
	 */
	public <T> void dropTable(Class<T> cls) throws DBException;

	/**
	 * 通过映射删除多个表
	 * 
	 * @param classes
	 *            映射类类型集合
	 */
	public <T> void dropTables(Class<T>[] classes) throws DBException;
	
	/**
	 * 在表里添加列
	 * @param cls
	 * @throws DBException
	 */
	public <T> void AddColumn(Class<T> cls, String columnName) throws DBException;

	/**
	 * 判断哪一个字段是否存在
	 * @param cls
	 * @param columnName
	 * @return
	 */
	public <T> boolean isExistColumn(Class<T> cls, String columnName);
}
