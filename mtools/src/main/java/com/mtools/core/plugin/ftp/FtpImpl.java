//package com.mtools.core.plugin.ftp;
//
//
//import it.sauronsoftware.ftp4j.FTPClient;
//import it.sauronsoftware.ftp4j.FTPFile;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Properties;
//import java.util.Vector;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
//
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.ChannelSftp.LsEntry;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;
//import com.jcraft.jsch.SftpATTRS;
//import com.mtools.core.plugin.helper.FuncUtil;
//
///**
// * 通用ftp模块操作类
// */
//public class FtpImpl implements FtpInf
//{
//	static Log log = LogFactory.getLog(FtpImpl.class);
//	//ftp详细配置参数
//	private FtpDetailConfig ftpparam = null;
//	//ftp操作类型 1-下载 2-上传
//	private int opetype;
//	private Session sshSession ;
//	private FTPClient ftpclient;
//	private ChannelSftp sftpclient = null;
//	private boolean autoColseConnection = true;
//	private boolean usecustompath=false;   			//用自定义路径
//	private String customuppath;				    //自定义上传路径
//	private String customdownpath;                  //自定义下载路径
//	private String encoding=null;
//
//	public FtpDetailConfig getFtpparam() {
//		return ftpparam;
//	}
//
//	public void setFtpparam(FtpDetailConfig ftpparam) {
//		this.ftpparam = ftpparam;
//	}
//
//	public int getOpetype() {
//		return opetype;
//	}
//
//	public void setOpetype(int opetype) {
//		this.opetype = opetype;
//	}
//
//	public FTPClient getFtpclient() {
//		return ftpclient;
//	}
//
//	public void setFtpclient(FTPClient ftpclient) {
//		this.ftpclient = ftpclient;
//	}
//
//	public ChannelSftp getSftpclient() {
//		return sftpclient;
//	}
//
//	public void setSftpclient(ChannelSftp sftpclient) {
//		this.sftpclient = sftpclient;
//	}
//
//	public FtpImpl(FtpDetailConfig detailConfig,int opetype)
//	{
//		this.ftpparam = detailConfig;
//		this.opetype = opetype;
//	}
//
//	public boolean isAutoColseConnection() {
//		return autoColseConnection;
//	}
//
//	public void setAutoColseConnection(boolean autoColseConnection) {
//		this.autoColseConnection = autoColseConnection;
//	}
//
//	public void setEncoding(String encoding) {
//		this.encoding = encoding;
//	}
//
//	/**
//	 * 连接FTP服务器
//	 * @return
//	 */
//	public boolean connectHost()
//	{
//		boolean result = false;
//		try
//		{
//			if(ftpparam.getProtocol()!=null&&ftpparam.getProtocol().toLowerCase().equals("sftp")){
//				JSch jsch = new JSch();
//				JSch.setLogger(new JschLogger());
//				jsch.getSession(ftpparam.getFtpuser(), ftpparam.getFtphost(), ftpparam.getFtpport());
//				sshSession = jsch.getSession(ftpparam.getFtpuser(), ftpparam.getFtphost(), ftpparam.getFtpport());
//				log.info("SFTP会话开始......");
//				sshSession.setPassword(ftpparam.getFtppwd());
//				Properties sshConfig = new Properties();
//				sshConfig.put("StrictHostKeyChecking", "no");
//				sshSession.setConfig(sshConfig);
//				sshSession.connect();
//				log.info("SFTP会话已连接......");
//				log.info("SFTP准备打开通道......");
//				Channel channel = sshSession.openChannel("sftp");
//				channel.connect();
//				sftpclient = (ChannelSftp) channel;
//
//				log.info("服务器当前路径:"+sftpclient.getHome());
////				Vector list_dir = sftpclient.ls(sftpclient.getHome());
////				if(list_dir!=null){
////					for(int i=0;i<list_dir.size();i++)
////						log.info("当前路径中文件:"+list_dir.get(i).toString());
////				}
//				if(opetype==FtpConstants.FTP_DOWNLOAD){
//					log.info("SFTP设置工作目录:"+ftpparam.getDownremotepath());
//					if(!FuncUtil.isEmpty(ftpparam.getDownremotepath())){
//						sftpclient.cd(ftpparam.getDownremotepath());
//					}
//				}
//				else if(opetype==FtpConstants.FTP_UPLOAD){
//					log.info("SFTP设置工作目录:"+ftpparam.getUpremotepath());
//					if(!FuncUtil.isEmpty(ftpparam.getUpremotepath())){
//						sftpclient.cd(ftpparam.getUpremotepath());
//					}
//				}
//			}
//			else{
//				TrustManager[] trustManager = new TrustManager[] { new X509TrustManager() {
//					public X509Certificate[] getAcceptedIssuers() {
//						return null;
//					}
//					public void checkClientTrusted(X509Certificate[] certs, String authType) {
//					}
//					public void checkServerTrusted(X509Certificate[] certs, String authType) {
//					}
//				} };
//				SSLContext sslContext = null;
//				try {
//					sslContext = SSLContext.getInstance("SSL");
//					sslContext.init(null, trustManager, new SecureRandom());
//				} catch (NoSuchAlgorithmException e) {
//					e.printStackTrace();
//				} catch (KeyManagementException e) {
//					e.printStackTrace();
//				}
//				SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//				
//				ftpclient = new FTPClient();
//				if(ftpparam.getProtocol()!=null&&ftpparam.getProtocol().toLowerCase().equals("ftps")){
//					ftpclient.setSSLSocketFactory(sslSocketFactory);
//					ftpclient.setSecurity(FTPClient.SECURITY_FTPS);
//				}
//				else if(ftpparam.getProtocol()!=null&&ftpparam.getProtocol().toLowerCase().equals("ftpes")){
//					ftpclient.setSSLSocketFactory(sslSocketFactory);
//					ftpclient.setSecurity(FTPClient.SECURITY_FTPES);
//				}
//				ftpclient.connect(ftpparam.getFtphost(), ftpparam.getFtpport());
//				if(!ftpclient.isConnected()){
//					log.error("连接FTP服务器失败--->"+ftpparam.getFtphost()+":"+ftpparam.getFtpport());
//					return result;
//				}
//				ftpclient.login(ftpparam.getFtpuser(), ftpparam.getFtppwd());
//				
//				if(ftpparam.getFtpmode().toLowerCase().equals("pasv"))
//					ftpclient.setPassive(true);
//				else
//					ftpclient.setPassive(false);
////				log.info("服务器当前路径:"+ftpclient.currentDirectory());
////				String[] list_dir = ftpclient.listNames();
////				if(list_dir!=null){
////					for(int i=0;i<list_dir.length;i++)
////						log.info("当前路径中文件:"+list_dir[i]);
////				}
//				if(opetype==FtpConstants.FTP_DOWNLOAD&&ftpparam.getDownremotepath()!=null){
//					log.info("FTP设置工作目录:"+ftpparam.getDownremotepath());
//					ftpclient.changeDirectory(ftpparam.getDownremotepath());
//				}
//				else if(opetype==FtpConstants.FTP_UPLOAD&&ftpparam.getUpremotepath()!=null){
//					log.info("FTP设置工作目录:"+ftpparam.getUpremotepath());
//					ftpclient.changeDirectory(ftpparam.getUpremotepath());
//				}
//				ftpclient.setType(FTPClient.TYPE_BINARY);	
//				if(!FuncUtil.isEmpty(encoding)) //add by mofu 加入自定义编码
//				{
//					ftpclient.setCharset(encoding);
//					log.info("已改变字符编码为:"+this.encoding);
//				}
//			}
//			
//			log.info((new StringBuilder("Ftp服务器连接成功:")).append(ftpparam.getFtphost()).append(":").append(ftpparam.getFtpport()).toString());
//			result = true;
//		}
//		catch (Exception ex)
//		{
//			log.error("连接FTP服务器错误,请检查配置参数和网络",ex);
//			result = false;
//		}
//		return result;
//	}
//
//	/**
//	 * 关闭连接
//	 */
//	public void closeConnect()
//	{
//		try
//		{
//			if (ftpclient != null)
//			{
//				ftpclient.disconnect(true);
//				ftpclient = null;
//				log.info("Ftp Disconnect Success");
//			}
//			if(sftpclient!=null){
//				sshSession.disconnect() ;
//				sftpclient.disconnect();
//				sftpclient=null;
//				log.info("SFTP Disconnect Success");
//			}
//		}
//		catch (Exception ex)
//		{
//			log.error("关闭FTP连接错误",ex);
//		}
//	}
//
//	/**
//	 * 上传单文件
//	 * @param localfile    本地文件,设置好localpath后仅设置文件名
//	 * @param remotefile   远程文件,设置好remotepath后仅设置文件名
//	 * @return
//	 */
//	public boolean uploadFile(String localfile, String remotefile)
//	{
//		boolean result = false;
//		try{
//			if (localfile == null || localfile.trim().length()== 0){
//				log.error("本地文件参数错误,无法上传");
//				return result;
//			}
//			if (remotefile == null || remotefile.trim().length() == 0){
//				log.error("远程文件参数错误,无法上传");
//				return result;			
//			}		
//			if (ftpclient== null&&sftpclient==null){
//				log.error("ftp服务器尚未连接,无法上传");
//				return result;
//			}	
//			//设置全局路径
//			if(usecustompath)
//			{
//				if(!customuppath.endsWith(File.separator))
//					localfile = customuppath+File.separator+localfile;
//				else
//					localfile = customuppath+localfile;
//			}else{
//				if(ftpparam.getUplocalpath()!=null&&ftpparam.getUplocalpath().trim().length()>0){
//					if(!ftpparam.getUplocalpath().endsWith(File.separator))
//						localfile = ftpparam.getUplocalpath()+File.separator+localfile;
//					else
//						localfile = ftpparam.getUplocalpath()+localfile;
//				}
//			}
//
//			File file;
//			file = new File(localfile);
//			if (!file.exists()){
//				log.error("本地文件不存在,无法上传");
//				return result;
//			}		
//			if(ftpclient!=null){
//				try
//				{
//					//ftpclient.setCharset("gbk"); 
//					//log.info("已设置为gbk编码格式..................");
//					ftpclient.upload(file);
//					String[] temp = localfile.split("/");
//					if(!temp[temp.length-1].equals(remotefile))
//						ftpclient.rename(temp[temp.length-1], remotefile);
//					//ftpclient.upload(new File(localfile));
//					log.info((new StringBuilder("上传文件:")).append(localfile).append(" 至ftp服务器:").append(remotefile).append(" 成功").toString());
//					result=true;
//				}
//				catch (Exception ex)
//				{
//					log.error((new StringBuilder("上传文件:")).append(localfile).append(" 至ftp服务器:").append(remotefile).append(" 失败").toString(),ex);
//					result = false;
//				}			
//			}
//			else if(sftpclient!=null){
//				try{
//					InputStream is = new FileInputStream(file);
//					sftpclient.put(is,remotefile);
//					log.info((new StringBuilder("上传文件:")).append(localfile).append(" 至sftp服务器:").append(remotefile).append(" 成功").toString());
//					result = true;
//					is.close();
//				}
//				catch(Exception ex){
//					log.error((new StringBuilder("上传文件:")).append(localfile).append(" 至sftp服务器:").append(remotefile).append(" 失败").toString(),ex);
//					result = false;
//				}
//			}
//		}
//		catch(Exception ex){
//			log.error("上传异常",ex);
//			result = false;
//		}
//		finally{
//			if(autoColseConnection){
//				this.closeConnect();
//			}
//		}
//		return result;
//	}
//	
//	/**
//	 * 上传队列文件
//	 * @param localfile    本地文件队列,设置好localpath后仅设置文件名
//	 * @param remotefile   远程文件队列,设置好remotepath后仅设置文件名
//	 * @return
//	 */
//	public boolean uploadFile(List<String> localfile, List<String> remotefile)
//	{
//		boolean result = false;
//		try{
//			if (localfile == null || localfile.size()==0){
//				log.error("本地文件队列参数错误,无法上传");
//				return result;
//			}
//			if (remotefile == null || remotefile.size() == 0){
//				log.error("远程文件队列参数错误,无法上传");
//				return result;			
//			}
//			if(localfile.size()!=remotefile.size()){
//				log.error("文件队列中本地和远程的文件个数不符");
//				return result;
//			}
//			if (ftpclient== null&&sftpclient==null){
//				log.error("ftp服务器尚未连接,无法上传");
//				return result;
//			}
//			if(ftpclient!=null){
//				try
//				{
//					for(int i=0;i<localfile.size();i++){
//						String sub_localfile = localfile.get(i);
//						if(usecustompath)
//						{
//							if(!customuppath.endsWith(File.separator))
//								sub_localfile = customuppath+File.separator+sub_localfile;
//							else
//								sub_localfile = customuppath+sub_localfile;
//						}else{
//							if(ftpparam.getUplocalpath()!=null&&ftpparam.getUplocalpath().trim().length()>0){
//								if(!ftpparam.getUplocalpath().endsWith(File.separator))
//									sub_localfile = ftpparam.getUplocalpath()+File.separator+sub_localfile;
//								else
//									sub_localfile = ftpparam.getUplocalpath()+sub_localfile;
//							}
//						}
//						File file = new File(sub_localfile);
//						if(!file.exists()){
//							log.info("本地文件:"+sub_localfile+" 不存在,跳过此文件");
//							continue;
//						}
//						ftpclient.upload(new File(sub_localfile));
//						log.info((new StringBuilder("上传文件:")).append(sub_localfile).append(" 至ftp服务器:").append(remotefile.get(i)).append(" 成功").toString());
//					}
//					result=true;
//				}
//				catch (Exception ex)
//				{
//					log.error("上传文件至ftp服务器部分或全部失败",ex);
//					result = false;
//				}			
//			}
//			else if(sftpclient!=null){
//				try
//				{
//					for(int i=0;i<localfile.size();i++){
//						String sub_localfile = localfile.get(i);
//						if(usecustompath)
//						{
//							if(!customuppath.endsWith(File.separator))
//								sub_localfile = customuppath+File.separator+sub_localfile;
//							else
//								sub_localfile = customuppath+sub_localfile;
//						}else{
//							if(ftpparam.getUplocalpath()!=null&&ftpparam.getUplocalpath().trim().length()>0){
//								if(!ftpparam.getUplocalpath().endsWith(File.separator))
//									sub_localfile = ftpparam.getUplocalpath()+File.separator+sub_localfile;
//								else
//									sub_localfile = ftpparam.getUplocalpath()+sub_localfile;
//							}
//						}
//						File file = new File(sub_localfile);
//						if(!file.exists()){
//							log.info("本地文件:"+sub_localfile+" 不存在,跳过此文件");
//							continue;
//						}
//						InputStream is = new FileInputStream(file);
//						sftpclient.put(is,remotefile.get(i));
//						//sftpclient.put(new FileInputStream(file),remotefile.get(i));
//						
//						log.info((new StringBuilder("上传文件:")).append(sub_localfile).append(" 至sftp服务器:").append(remotefile.get(i)).append(" 成功").toString());
//					}
//					result=true;
//				}
//				catch (Exception ex)
//				{
//					log.error("上传文件至sftp服务器部分或全部失败",ex);
//					result = false;
//				}						
//			}
//		}
//		catch(Exception ex){
//			log.error("上传异常",ex);
//			result = false;
//		}
//		finally{
//			if(autoColseConnection){
//				this.closeConnect();
//			}
//		}
//		return result;
//	}	
//
//	/**
//	 * 下载单文件
//	 * @param localfile    本地文件名,设置localpath后此处仅设置文件名
//	 * @param remotefile   远程文件路径,设置remotepath后此处仅设置文件名
//	 * @return
//	 */
//	public boolean downloadFile(String localfile, String remotefile)
//	{
//		boolean result = false;
//		try{
//			if (localfile == null || localfile.trim().length() == 0){
//				log.error("本地文件参数错误,无法下载");
//				return result;
//			}
//			if (remotefile == null|| remotefile.trim().length() == 0){
//				log.error("远程文件参数错误,无法下载");
//				return result;
//			}		
//			if (ftpclient == null&&sftpclient==null){
//				log.error("ftp服务器尚未连接,无法下载");
//				return result;
//			}
//			if(ftpclient!=null){
//				try
//				{
//					//设置全路径
//					if(usecustompath)
//					{
//						if(!customdownpath.endsWith(File.separator))
//							localfile = customdownpath+File.separator+localfile;
//						else
//							localfile = customdownpath+localfile;
//					}else{
//						if(ftpparam.getDownlocalpath()!=null&&ftpparam.getDownlocalpath().trim().length()>0){
//							if(!ftpparam.getDownlocalpath().endsWith(File.separator))
//								localfile = ftpparam.getDownlocalpath()+File.separator+localfile;
//							else
//								localfile = ftpparam.getDownlocalpath()+localfile;
//						}
//					}
//					
//					ftpclient.download(remotefile, new File(localfile));
//					log.info((new StringBuilder("下载文件:")).append(localfile).append(" 从ftp服务器:").append(remotefile).append(" 成功").toString());
//					result=true;
//				}
//				catch (Exception ex)
//				{
//					log.error((new StringBuilder("下载文件:")).append(localfile).append(" 从ftp服务器:").append(remotefile).append(" 失败").toString(),ex);
//					result = false;
//				}
//			}
//			else if(sftpclient!=null){
//				try{
//					//设置全路径
//					if(usecustompath)
//					{
//						if(!customdownpath.endsWith(File.separator))
//							localfile = customdownpath+File.separator+localfile;
//						else
//							localfile = customdownpath+localfile;
//					}else{
//						if(ftpparam.getDownlocalpath()!=null&&ftpparam.getDownlocalpath().trim().length()>0){
//							if(!ftpparam.getDownlocalpath().endsWith(File.separator))
//								localfile = ftpparam.getDownlocalpath()+File.separator+localfile;
//							else
//								localfile = ftpparam.getDownlocalpath()+localfile;
//						}
//					}
//					sftpclient.get(remotefile,new FileOutputStream(localfile));
//					log.info((new StringBuilder("下载文件:")).append(localfile).append(" 从sftp服务器:").append(remotefile).append(" 成功").toString());
//					result = true;
//				}
//				catch (Exception ex)
//				{
//					log.error((new StringBuilder("下载文件:")).append(localfile).append(" 从sftp服务器:").append(remotefile).append(" 失败").toString(),ex);
//					result = false;
//				}	
//			}
//		}
//		catch(Exception ex){
//			log.error("下载异常",ex);
//			result = false;
//		}
//		finally{
//			if(autoColseConnection){
//				this.closeConnect();
//			}
//		}
//		return result;
//	}
//	
//	/**
//	 * 下载队列文件
//	 * @param localfile    本地文件队列,设置localpath后此处仅设置文件名
//	 * @param remotefile   远程文件队列,设置remotepath后此处仅设置文件名
//	 * @return
//	 */
//	public boolean downloadFile(List<String> localfile, List<String> remotefile)
//	{
//		boolean result = false;
//		try{
//			if (localfile == null || localfile.size() == 0){
//				log.error("本地文件参数错误,无法下载");
//				return result;
//			}
//			if (remotefile == null || remotefile.size() == 0){
//				log.error("远程文件参数错误,无法下载");
//				return result;
//			}
//			if(localfile.size()!=remotefile.size()){
//				log.error("文件队列中本地和远程的文件个数不符");
//				return result;
//			}
//			if (ftpclient == null&&sftpclient==null){
//				log.error("ftp服务器尚未连接,无法下载");
//				return result;
//			}
//			if(ftpclient!=null){
//				try
//				{
//					for(int i=0;i<remotefile.size();i++){
//						String sub_localfile = localfile.get(i);
//						if(usecustompath)
//						{
//							if(!customdownpath.endsWith(File.separator))
//								sub_localfile = customdownpath+File.separator+sub_localfile;
//							else
//								sub_localfile = customdownpath+sub_localfile;
//						}else{
//							if(ftpparam.getDownlocalpath()!=null&&ftpparam.getDownlocalpath().trim().length()>0){
//								if(!ftpparam.getDownlocalpath().endsWith(File.separator))
//									sub_localfile = ftpparam.getDownlocalpath()+File.separator+localfile.get(i);
//								else
//									sub_localfile = ftpparam.getDownlocalpath()+localfile.get(i);
//							}
//						}
//
//						ftpclient.download(remotefile.get(i),new File(sub_localfile));
//						log.info((new StringBuilder("下载文件:")).append(localfile).append(" 从ftp服务器:").append(remotefile).append(" 成功").toString());
//					}
//					result=true;
//				}
//				catch (Exception ex)
//				{
//					log.error("下载文件从ftp服务器部分或全部失败",ex);
//					result = false;
//				}
//			}
//			else if(sftpclient!=null){
//				try
//				{
//					for(int i=0;i<remotefile.size();i++){
//						String sub_localfile = localfile.get(i);
//						if(usecustompath)
//						{
//							if(!customdownpath.endsWith(File.separator))
//								sub_localfile = customdownpath+File.separator+sub_localfile;
//							else
//								sub_localfile = customdownpath+sub_localfile;
//						}else{
//							if(ftpparam.getDownlocalpath()!=null&&ftpparam.getDownlocalpath().trim().length()>0){
//								if(!ftpparam.getDownlocalpath().endsWith(File.separator))
//									sub_localfile = ftpparam.getDownlocalpath()+File.separator+localfile.get(i);
//								else
//									sub_localfile = ftpparam.getDownlocalpath()+localfile.get(i);
//							}
//						}
//						sftpclient.get(remotefile.get(i),new FileOutputStream(sub_localfile));
//						log.info((new StringBuilder("下载文件:")).append(localfile).append(" 从sftp服务器:").append(remotefile).append(" 成功").toString());
//					}
//					result=true;
//				}
//				catch (Exception ex)
//				{
//					log.error("下载文件从sftp服务器部分或全部失败",ex);
//					result = false;
//				}			
//			}
//		}
//		catch(Exception ex){
//			log.error("下载异常",ex);
//			result = false;
//		}
//		finally{
//			if(autoColseConnection){
//				this.closeConnect();
//			}
//		}
//		return result;
//	}	
//	
//	/**
//	 * 列出目录下文件列表[仅列出列表]
//	 * @param remotepath   设置ftp参数的remotepath后此处可不设置
//	 * @return List<String>
//	 */
//	public List<String> listFilesOnly(){
//		try{
//			List<String> ls = null;
//			if(this.ftpclient!=null){
//				FTPFile[] ffarrs = ftpclient.list();
//				if(ffarrs==null||ffarrs.length==0){
//					log.debug("FTP当前目录下没有文件");
//					return null;
//				}
//				for(int i=0;i<ffarrs.length;i++){
//					FTPFile ff = ffarrs[i];
//					if(ff.getType()!=FTPFile.TYPE_FILE){
//						log.debug("文件:"+ff.getName()+" 为目录或连接");
//						continue;
//					}
//					if(ls==null)ls=new ArrayList<String>();
//					ls.add(ff.getName());
//				}
//			}
//			else if(this.sftpclient!=null){
//				ls = new ArrayList<String>();
//				Vector<LsEntry> vector = sftpclient.ls("./");
//				if(vector==null) vector = new Vector<LsEntry>();
//				for(LsEntry lsEntry:vector){
//					SftpATTRS  attr=lsEntry.getAttrs();
//					if(!attr.isDir())
//					{
//						ls.add(lsEntry.getFilename());
//					}
//				}
//			}
//			return ls;
//		}
//		catch(Exception ex){
//			log.error("列出ftp下当前文件失败",ex);
//			return null;
//		}
//	}
//	
//	/**
//	 * 列出目录下文件列表[仅列出列表]
//	 * @param remotepath   设置ftp参数的remotepath后此处可不设置
//	 * @return List<String>
//	 */
//	public FTPFile[] listFiles(){
//		try{
//			if(this.ftpclient!=null){
//				FTPFile[] ffarrs = ftpclient.list();
//				if(ffarrs==null||ffarrs.length==0){
//					log.debug("FTP当前目录下没有文件");
//					return null;
//				}
//				return ffarrs;
//			}
//			else if(this.sftpclient!=null){
//			}
//			return null;
//		}
//		catch(Exception ex){
//			log.error("列出ftp下当前文件失败",ex);
//			return null;
//		}
//	}	
//	
//	//获取文件夹大小
//	public Long getDirsSize() throws Exception {
//		Long result=0L;
//		if(this.ftpclient!=null)
//		{
//			FTPFile[] ffarrs = ftpclient.list();
//			for(FTPFile file : ffarrs)
//			{
//				result+=file.getSize();
//			}
//		}
//		return result;
//	}
//	
//	/**
//	 * 列出目录下文件列表[包括目录]
//	 * @param remotepath   设置ftp参数的remotepath后此处可不设置
//	 * @return List<String>
//	 */
//	public List<String> listFilesAndDirs(){
//		try{
//			List<String> ls = null;
//			if(this.ftpclient!=null){
//				String[] ffarrs = ftpclient.listNames();
//				for(int i=0;i<ffarrs.length;i++){
//					if(ls==null)ls=new ArrayList<String>();
//					ls.add(ffarrs[i]);
//				}
//			}
//			else if(this.sftpclient!=null){
//				ls = new ArrayList<String>();
//				Vector<LsEntry> vector = sftpclient.ls("./");
//				if(vector==null) vector = new Vector<LsEntry>();
//				for(LsEntry lsEntry:vector){
//					ls.add(lsEntry.getFilename());
//				}
//			}
//			return ls;
//		}
//		catch(Exception ex){
//			log.error("列出ftp下当前文件失败",ex);
//			return null;
//		}
//	}
//	
//	/**
//	 * 当前路径下重命名 oldname和newname均相对于当前路径
//	 * @param oldname
//	 * @param newname
//	 * @return
//	 */
//	public boolean rename(String oldname,String newname){
//		try{
//			if(this.ftpclient!=null){
//				this.ftpclient.rename(oldname, newname);
//			}
//			else if(this.sftpclient!=null){
//				this.sftpclient.rename(oldname, newname);
//			}
//			return true;
//		}
//		catch(Exception ex){
//			log.error("重命名文件失败",ex);
//			return false;
//		}
//	}
//	
//	
//	/**
//	 * 当前路径下删除 filename相对于当前路径
//	 * @param filename
//	 * @return
//	 */
//	public boolean remove(String filename){
//		try{
//			if(this.ftpclient!=null){
//				this.ftpclient.deleteFile(filename);
//			}
//			else if(this.sftpclient!=null){
//				this.sftpclient.rm(filename);
//			}
//			return true;
//		}
//		catch(Exception ex){
//			log.error("删除文件失败",ex);
//			return false;
//		}
//	}
//	
//	/**
//	 * 当前路径下删除 文件夹
//	 * @param filename
//	 * @return
//	 */
//	public boolean removedir(String dirname)
//	{
//		try{
//			if(this.ftpclient!=null){
//				this.ftpclient.deleteDirectory(dirname);
//			}
//			else if(this.sftpclient!=null){
//				this.sftpclient.rmdir(dirname);
//			}
//			return true;
//		}
//		catch(Exception ex){
//			log.error("删除文件夹失败",ex);
//			return false;
//		}
//	}
//	
//	/**
//	 * 当前路径下新建目录 dirname相对于当前路径
//	 * @param dirname
//	 * @return
//	 */
//	public boolean mkdir(String dirname){
//		try{
//			if(this.ftpclient!=null){
//				this.ftpclient.createDirectory(dirname);
//			}
//			else if(this.sftpclient!=null){
//				this.sftpclient.mkdir(dirname);
//			}
//			return true;
//		}
//		catch(Exception ex){
//			log.error("删除文件失败",ex);
//			return false;
//		}
//	}
//	
//	/**
//	 * 当前路径下新建目录(支持多级目录)并变换工作路径至新建目录下 dirname相对于当前路径
//	 * @param dirname
//	 * @return
//	 */
//	public boolean mkdirandcd2newdir(String dirname){
//		try{
//			String[] dirs = dirname.split("/");
//			for(String dir:dirs){
//				if(FuncUtil.isEmpty(dir))
//				{
//					continue;
//				}
//				if(!this.fileExistCheck(dir)){
//					if(this.ftpclient!=null){
//						this.ftpclient.createDirectory(dir);
//					}
//					else if(this.sftpclient!=null){
//						this.sftpclient.mkdir(dir);
//					}
//				}
//				this.cd(dir);
//			}
//			return true;
//		}
//		catch(Exception ex){
//			log.error("删除文件失败",ex);
//			return false;
//		}
//	}	
//	
//	public boolean dirsExistCheck(String dir)
//	{
//		try{
//			String[] dirs = dir.split("/");
//			
//			for(String str : dirs)
//			{
//				if(str==null)
//				{
//					continue;
//				}
//				if(!this.fileExistCheck(dir))
//				{
//					return false;
//				}
//				this.cd(str);
//			}
//			return true;
//		}catch(Exception ex)
//		{
//			log.error("检测目录失败:"+ex.getMessage(), ex);
//			return false;
//		}
//	}
//	
//	/**
//	 * 变换路径 dirname相对于FTP根目录的绝对路径
//	 * @param dirname
//	 * @return
//	 */
//	public boolean cd(String dirname){
//		try{
//			if(this.ftpclient!=null){
//				this.ftpclient.changeDirectory(dirname);
//			}
//			else if(this.sftpclient!=null){
//				this.sftpclient.cd(dirname);
//			}
//			return true;
//		}
//		catch(Exception ex){
//			log.error("删除文件失败",ex);
//			return false;
//		}
//	}	
//	
//	/**
//	 * 文件存在性检查
//	 * @param filename
//	 * @return boolean
//	 * @author l_ghui
//	 * @date 2009-10-12
//	 */
//	public boolean fileExistCheck(String filename){
//		boolean result=false;
//		try{
//			if(this.ftpclient!=null){
//				String[] file_list = this.ftpclient.listNames();
//				for(int i=0;i<file_list.length;i++){
//					if(file_list[i].equals(filename))
//						return true;
//				}
//			}else if(this.sftpclient!=null){
//				Vector<LsEntry> vector = sftpclient.ls("./");
//				if(vector==null) vector = new Vector<LsEntry>();
//				for(LsEntry lsEntry:vector){					
//					if(filename.equals(lsEntry.getFilename())) 
//						return true;
//				}
//			}else{
//				log.info("服务器尚未连接,无法下载");
//			}
//			
//		}
//		catch(Exception ex){
//			ex.printStackTrace();
//		}
//		return result;
//	}
//	
//	
//
//	public FtpDetailConfig getftpparam() {
//		return ftpparam;
//	}
//
//	public void setftpparam(FtpDetailConfig ftpparam) {
//		this.ftpparam = ftpparam;
//	}
//
//	public boolean isUsecustompath() {
//		return usecustompath;
//	}
//
//	public void setUsecustompath(boolean usecustompath) {
//		this.usecustompath = usecustompath;
//	}
//	
//	public void setCustomapth(String customuppath,String customdownpath)
//	{
//		this.customuppath=customuppath;
//		this.customdownpath=customdownpath;
//	}
//}
