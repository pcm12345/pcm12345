# -*- coding: utf-8 -*-
import swiftclient
from datetime import datetime, timedelta
import os, time, re
import setting
from keystoneauth1 import session
from keystoneauth1.identity import v3
from apscheduler.schedulers.background import BackgroundScheduler
sched = BackgroundScheduler()

#인증정보
access_key = 'E5A5A18C52E30340EB35'
secret_key = '564344AFCBF1873637D4ABA2B75CF60677EBDA42'
domain_id = 'default'
project_id = 'fa94375f1d28417c8d880e6f4a6fde31'

#bucket 정보
container_name = 'backup'

#데이터베이스 정보
ip = setting.dbinfo["imooc"]["host"]
port = setting.dbinfo["imooc"]["port"]
database_id = setting.dbinfo["imooc"]["user"]
database_pwd = setting.dbinfo["imooc"]["password"]

#백업파일 경로
#file_path = '/home/carrot/backup_py/'
file_path = 'C:/Users/D146/backup_py/'


bucket_name = 'imooc'
content_type = 'text/plain'
directory = '/home/carrot/backup_py/'
ym = datetime.now().strftime("%Y-%m")
ymd = datetime.now().strftime("%Y-%m-%d")

def oracle_pump(_uid, _password, _schema_name, _directory):
    filename = f'{_schema_name}_{ymd}'
    # filename = '{}_{}'.format(_schema_name,ymd)
    # command = f'expdp {_uid}/{_password} schemas={_schema_name} directory={_directory}' \
    #           f' dumpfile={filename}.dmp logfile=.log '
    command = 'expdp {}/{} schemas={} directory={}' \
              'dumpfile={}.dmp logfile=.log'.format(_uid, _password, _schema_name, _directory, filename)                        
    os.system(command)


def mysql_pump(databasename):
    uid = setting.dbinfo[databasename]["user"]
    password = setting.dbinfo[databasename]["password"]
    ip = setting.dbinfo[databasename]["host"]
    port = setting.dbinfo[databasename]["port"]

    # filename = f'{databasename}_{ymd}'
    filename = '{}_{}'.format(databasename,ymd)
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Database Backup Start')
    # command = f'mysqldump -h {ip} -u {uid} -p{password} -P {port} --default-character-set=utf8 --compress --all_databases | gzip > {file_path}{filename}.gz'
    command = 'mysqldump -h {} -u {} -p{} -P {} --default-character-set=utf8 --compress --all_databases | gzip > {}{}.gz'.format(ip, uid, password, port, file_path, filename)
    os.system(command)
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Database Backup End')

    # 백업파일 분할 1000M
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Split Backup File Start')
    # command = f'split -b 1000m {file_path}{filename}.gz {file_path}{filename}.gz.part_'
    command = 'split -b 1000m {}{}.gz {}{}.gz.part_'.format(file_path, filename, file_path, filename)
    os.system(command)
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Split Backup File End')

    # 오리지널 백업파일 삭제
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Origin Backup File Delete')
    # command = f'rm -rf {file_path}{filename}.gz'
    command = 'rm -rf {}{}.gz'.format(file_path, filename)
    os.system(command)
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Database Backup End')

    # 네이버 클라우드 업로드
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Archive Storage Upload Start')
    swift_connection = init_swift_connection(access_key, secret_key, domain_id, project_id)
    # upload(swift_connection, container_name, databasename, f'{filename}', content_type, file_path)
    upload(swift_connection, container_name, databasename, '{}'.format(filename), content_type, file_path)
    print('['+datetime.now().strftime("%Y-%m-%d %H:%M:%S")+'] '+databasename+' Archive Storage Upload End')


def init_swift_connection(_accessKey, _secretKey, _domainId, _projectId):
    endpoint = 'https://kr.archive.ncloudstorage.com:5000/v3'
    auth = v3.Password(auth_url=endpoint, username=_accessKey, password=_secretKey
                       , project_id=_projectId, user_domain_id=_domainId)
    auth_session = session.Session(auth=auth)
    return swiftclient.Connection(retries=5, session=auth_session)


def upload(_swift_connection, _container_name, _bucket_name, _object_name, _content_type, _file_path):
    files = findfiles(_file_path, _object_name)
    for _file in files:
        with open(_file, 'rb') as local_file:
            filename = os.path.basename(_file)
            # _swift_connection.put_object(f'{_container_name}/{_bucket_name}/{ym}', filename, contents=local_file.read(), content_type=_content_type)
            _swift_connection.put_object('{}/{}/{}'.format(_container_name, _bucket_name, ym), filename, contents=local_file.read(), content_type=_content_type)
            os.remove(_file)


def findfiles(path, regex):
    regObj = re.compile(regex)
    res = []
    for root, dirs, f_names in os.walk(path):
        for f_name in f_names:
            if regObj.match(f_name):
                res.append(os.path.join(root, f_name))
    return res


def delete_object(_container_name, _file_name):
    swift_connection = init_swift_connection(access_key, secret_key, domain_id, project_id)
    # swift_connection.delete_object(_container_name, f'{bucket_name}/{ym}/{_file_name}')
    swift_connection.delete_object(_container_name, '{}/{}/{}'.format(bucket_name, ym, _file_name))


def find_object(_container_name):
    swift_connection = init_swift_connection(access_key, secret_key, domain_id, project_id)
    # container = swift_connection.get_container(f'{_container_name}')
    container = swift_connection.get_container('{}'.format(_container_name))
    for obj in container[1]:
        name = obj['name'].split('/')
        upload_date_string = (str(obj['last_modified'])[0:10])
        init_date = datetime.now() - timedelta(days=5)
        upload_date = datetime.strptime(upload_date_string, '%Y-%m-%d')
        #if(init_date > upload_date)delete_object(container_name, name[-1])



# @sched.scheduled_job('cron', hour='9', minute='0', id='imooc-course-data-sync')
# 매시 5분마다 스케쥴러 동작.
# @sched.scheduled_job('cron', hour='2', minute='0', id='database-backup-all')
# def job1():
#     try:
#         # mysql_pump("imooc_apply")
#         # mysql_pump("imooc")
#         # mysql_pump("ai-tutor")
#         # mysql_pump("b2b-apply-db")
#         # mysql_pump("catch")
#         # mysql_pump("hppk")
#         # mysql_pump("tts")
#         # mysql_pump("daily-writing-b2b")
#         # mysql_pump("carrot-platform")
#         # mysql_pump("coactive")
#         # mysql_pump("itm")
#         # mysql_pump("itm_sugang")
#         # mysql_pump("kuder_and_php")
#         # mysql_pump("sk")
#         # mysql_pump("ctm")
#     except Exception as e:
#         print (e)

# print(f'[{datetime.now().strftime("%Y-%m-%d %H:%M:%S")}] : [sched Begin]')
# sched.start()

# while True:
#     time.sleep(1)

@sched.scheduled_job('cron', hour='14', minute='11', id='database-backup-all')
def job2():
    try:
        mysql_pump("local_db")
    except Exception as e:
        print (e)

# print(f'[{datetime.now().strftime("%Y-%m-%d %H:%M:%S")}] : [sched Begin]')
print('[{}] : [sched Begin]'.format(datetime.now().strftime("%Y-%m-%d %H:%M:%S")))
sched.start()

while True:
    time.sleep(1)
