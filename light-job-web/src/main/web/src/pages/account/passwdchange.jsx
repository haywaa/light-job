import { Card, message } from 'antd';
import ProForm, {
  ProFormDateRangePicker,
  ProFormDependency,
  ProFormDigit,
  ProFormRadio,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-form';
import { useRequest } from 'umi';
import { PageContainer } from '@ant-design/pro-layout';
import { fakeSubmitForm } from './service';
import styles from './style.less';

const BasicForm = () => {
  const { run } = useRequest(fakeSubmitForm, {
    manual: true,
    onSuccess: () => {
      message.success('提交成功');
    },
  });

  const onFinish = async (values) => {
    run(values);
  };

  return (
    <PageContainer content="密码修改">
      <Card bordered={false}>
        <ProForm
          hideRequiredMark
          style={{
            margin: 'auto',
            marginTop: 8,
            maxWidth: 600,
          }}
          name="basic"
          layout="vertical"
          initialValues={{
            public: '1',
          }}
          onFinish={onFinish}
        >
          <ProFormText.Password
            width="md"
            label="当前密码"
            name="currentPasswd"
            rules={[
              {
                required: true,
                message: '请输入当前密码',
              },
            ]}
            placeholder="请输入当前密码"
          />
          <ProFormText.Password
            width="md"
            label="新密码"
            name="newPasswd"
            rules={[
              {
                required: true,
                message: '请输入新密码',
              },
            ]}
            placeholder="请输入新密码"
          />
          <ProFormText.Password
            width="md"
            label="确认新密码"
            name="newPasswd"
            rules={[
              {
                required: true,
                message: '请再次输入新密码',
              },
            ]}
            placeholder="请再次输入新密码"
          />
        </ProForm>
      </Card>
    </PageContainer>
  );
};

export default BasicForm;
