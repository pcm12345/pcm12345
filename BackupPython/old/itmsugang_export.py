import swiftclient
from datetime import datetime, timedelta
import os
import re
from keystoneauth1 import session
from keystoneauth1.identity import v3

#인증정보
access_key = '2E858C13330384789362'
secret_key = 'BBACC0879083616CA5CBC1552BA4AC8D8016405D'
domain_id = 'default'
project_id = 'fa94375f1d28417c8d880e6f4a6fde31'

#데이터베이스 정보
ip = '10.36.0.92'
port = '23306'
database_id = 'root'
database_pwd = 'carrot1324!#@$'
container_name = 'backup'
bucket_name = 'itm_sugang'
content_type = 'text/plain'
directory = '/home/carrot/backup_py/'
ym = datetime.now().strftime("%Y-%m")
ymd = datetime.now().strftime("%Y-%m-%d")

def oracle_pump(_uid, _password, _schema_name, _directory):
    filename = f'{_schema_name}_{ymd}'
    command = f'expdp {_uid}/{_password} schemas={_schema_name} directory={_directory}' \
              f' dumpfile={filename}.dmp logfile=.log '
    os.system(command)


def mysql_pump(_uid, _password, _schema_name, _directory, _ip, _port):
    filename = f'{_schema_name}_{ymd}'
    command = f'mysqldump -h {_ip} -u {_uid} -p{_password} -P {_port} --default-character-set=utf8 --compress --all_databases | gzip > {_directory}{filename}.gz'
    os.system(command)
    #print(command)
    swift_connection = init_swift_connection(access_key, secret_key, domain_id, project_id)
    upload(swift_connection, container_name, f'{filename}', content_type, _directory)


def init_swift_connection(_accessKey, _secretKey, _domainId, _projectId):
    endpoint = 'https://kr.archive.ncloudstorage.com:5000/v3'
    auth = v3.Password(auth_url=endpoint, username=_accessKey, password=_secretKey
                       , project_id=_projectId, user_domain_id=_domainId)
    auth_session = session.Session(auth=auth)
    return swiftclient.Connection(retries=5, session=auth_session)


def upload(_swift_connection, _container_name, _object_name, _content_type, _file_path):
    files = findfiles(_file_path, _object_name)
    for _file in files:
        with open(_file, 'rb') as local_file:
            filename = os.path.basename(_file)
            _swift_connection.put_object(f'{_container_name}/{bucket_name}/{ym}', filename, contents=local_file.read(), content_type=_content_type)
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
    swift_connection.delete_object(_container_name, f'{bucket_name}/{ym}/{_file_name}')


def find_object(_container_name):
    swift_connection = init_swift_connection(access_key, secret_key, domain_id, project_id)
    container = swift_connection.get_container(f'{_container_name}')
    for obj in container[1]:
        name = obj['name'].split('/')
        upload_date_string = (str(obj['last_modified'])[0:10])
        init_date = datetime.now() - timedelta(days=5)
        upload_date = datetime.strptime(upload_date_string, '%Y-%m-%d')
        #if(init_date > upload_date)delete_object(container_name, name[-1])

mysql_pump(database_id, database_pwd, bucket_name, directory, ip, port)
