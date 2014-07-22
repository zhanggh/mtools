package com.mtools.core.plugin.ftp;


import it.sauronsoftware.ftp4j.FTPClient;

import java.util.List;

import com.jcraft.jsch.ChannelSftp;

public interface FtpInf {

	public FtpDetailConfig getFtpparam();

	public void setFtpparam(FtpDetailConfig ftpparam);

	public int getOpetype();

	public void setOpetype(int opetype);

	public FTPClient getFtpclient();

	public void setFtpclient(FTPClient ftpclient);

	public ChannelSftp getSftpclient();

	public void setSftpclient(ChannelSftp sftpclient);
	
	public boolean isAutoColseConnection();

	public void setAutoColseConnection(boolean autoColseConnection);
	
	public void setEncoding(String encoding);

	/**
	 * 连接FTP服务器
	 * @return
	 */
	public boolean connectHost();

	/**
	 * 关闭连接
	 */
	public void closeConnect();

	/**
	 * 上传单文件
	 * @param localfile    本地文件,设置好localpath后仅设置文件名
	 * @param remotefile   远程文件,设置好remotepath后仅设置文件名
	 * @return
	 */
	public boolean uploadFile(String localfile, String remotefile);

	/**
	 * 上传队列文件
	 * @param localfile    本地文件队列,设置好localpath后仅设置文件名
	 * @param remotefile   远程文件队列,设置好remotepath后仅设置文件名
	 * @return
	 */
	public boolean uploadFile(List<String> localfile, List<String> remotefile);

	/**
	 * 下载单文件
	 * @param localfile    本地文件名,设置localpath后此处仅设置文件名
	 * @param remotefile   远程文件路径,设置remotepath后此处仅设置文件名
	 * @return
	 */
	public boolean downloadFile(String localfile, String remotefile);

	/**
	 * 下载队列文件
	 * @param localfile    本地文件队列,设置localpath后此处仅设置文件名
	 * @param remotefile   远程文件队列,设置remotepath后此处仅设置文件名
	 * @return
	 */
	public boolean downloadFile(List<String> localfile, List<String> remotefile);
	
	/**
	 * 列出目录下文件列表[仅列出列表]
	 * @param remotepath   设置ftp参数的remotepath后此处可不设置
	 * @return List<String>
	 */
	public List<String> listFilesOnly();
	
	/**
	 * 列出目录下文件列表[包括目录]
	 * @param remotepath   设置ftp参数的remotepath后此处可不设置
	 * @return List<String>
	 */
	/**
	 * 文件存在性检查
	 * @param filename
	 * @return boolean
	 */
	public boolean fileExistCheck(String m_fname);
	
	/**
	 * 检查路径存在
	 * @param dir
	 * @return
	 * @author: mofu
	 * @time: 2012-9-6 上午11:57:53
	 */
	public boolean dirsExistCheck(String dir);
	
	/**
	 * 当前路径下重命名 oldname和newname均相对于当前路径
	 * @param oldname
	 * @param newname
	 * @return
	 */
	public boolean rename(String oldname,String newname);
	
	/**
	 * 当前路径下删除 filename相对于当前路径
	 * @param filename
	 * @return
	 */
	public boolean remove(String filename);	
	
	/**
	 * 当前路径下删除 文件夹
	 * @param filename
	 * @return
	 */
	public boolean removedir(String dirname);	
	
	/**
	 * 当前路径下新建目录 dirname相对于当前路径
	 * @param dirname
	 * @return
	 */
	public boolean mkdir(String dirname);	
	
	/**
	 * 变换路径 dirname相对于FTP根目录的绝对路径
	 * @param dirname
	 * @return
	 */
	public boolean cd(String dirname);	
	
	public List<String> listFilesAndDirs();	

	public FtpDetailConfig getftpparam();

	public void setftpparam(FtpDetailConfig ftpparam);
	
	public boolean isUsecustompath();
	
	public void setUsecustompath(boolean val);
	
	public void setCustomapth(String customuppath,String customdownpath);
	
	public Long getDirsSize()throws Exception;
}