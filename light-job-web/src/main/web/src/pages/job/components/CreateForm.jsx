import React, {useEffect, useState} from 'react';
import {Card, Modal, Row, Col, Select, Form, Input, InputNumber, Button} from 'antd';
import { queryAllGroup } from '../service';

const FormItem = Form.Item;
const { Option } = Select;
const { TextArea } = Input;

const formLayout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const submitFormLayout = {
  wrapperCol: {
    xs: {
      span: 24,
      offset: 0,
    },
    sm: {
      span: 24,
      offset: 12,
    },
  },
};

const CreateForm = (props) => {
  const { onSubmit, modalVisible, onCancel } = props;
  const [form] = Form.useForm();
  const [groupList, setGroupList] = useState([]);
  useEffect(() => {
    queryAllGroup((data) => setGroupList(data));
  }, []);

  const renderBasicField = () => {
    return (
      <Card title="基础配置" bordered={false}>
        <Row gutter={8}>
          <Col span={12}>
            <FormItem
              label='执行器(应用)'
              key="jobGroup"
              name="jobGroup"
              rules={[
                {
                  required: true,
                  message: '请选择执行器',
                },
              ]}>
              <Select
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
              >
                {groupList.map((item) => <Option key={item.groupCode} value={item.groupCode}>{item.groupCode}</Option>)}
              </Select>
            </FormItem>
          </Col>
          <Col span={12}>
              <FormItem
                label='任务Handler'
                key="executorHandler"
                name="executorHandler"
                rules={[
                  {
                    required: true,
                    message: '请输入任务Code',
                  },
                ]}>
                <Input
                  style={{ width: '100%' }}
                  // disabled = {formVals.position !== ''}
                >
                  {/*{weightList.map((item) => <Option key={item} value={item}>{item}</Option>)}*/}
                </Input>
              </FormItem>
          </Col>
        </Row>
        <Row gutter={8}>
          <Col span={12}>
            <FormItem
              label='执行时长(秒)'
              key="executorTimeout"
              name="executorTimeout"
              rules={[
                {
                  required: true,
                  message: '请输入预估最大执行时长',
                },
              ]}>
              <InputNumber
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
                placeholder={"请输入预估最大执行时长"}
              >
                {/*{weightList.map((item) => <Option key={item} value={item}>{item}</Option>)}*/}
              </InputNumber>
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem
              label='任务参数'
              key="executorParam"
              name="executorParam"
              rules={[
                {
                  required: false,
                  message: '请输入任务参数',
                },
              ]}>
              <Input
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
                placeholder={"请输入任务参数"}
              >
                {/*{weightList.map((item) => <Option key={item} value={item}>{item}</Option>)}*/}
              </Input>
            </FormItem>
          </Col>
        </Row>
        <Row gutter={8}>
          <Col span={12}>
            <FormItem
              label='任务状态'
              key="status"
              name="status"
              rules={[
                {
                  required: true,
                  message: '请选择状态',
                },
              ]}>
              <Select
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
              >
                <Option key={"0"} value={"0"}>{"停止"}</Option>
                <Option key={"1"} value={"1"}>{"运行"}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span={12}>
          </Col>
        </Row>
      </Card>
    )
  }

  const renderSchelduleField = () => {
    return (
      <Card title="调度配置" bordered={false}>
        <Row gutter={16}>
          <Col span={12}>
            <FormItem
              label='调度类型'
              key="scheduleType"
              name="scheduleType"
              rules={[
                {
                  required: true,
                  message: '请选择执行器',
                },
              ]}>
              <Select
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
              >
                <Option key={"CRON"} value={"CRON"}>{"CRON"}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem
              label='CRON'
              key="scheduleConf"
              name="scheduleConf"
              rules={[
                {
                  required: true,
                  message: '请输入CRON表达式',
                },
              ]}>
              <Input
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
              >
                {/*{weightList.map((item) => <Option key={item} value={item}>{item}</Option>)}*/}
              </Input>
            </FormItem>
          </Col>
        </Row>
      </Card>
    )
  }

  const renderAdvanceField = () => {
    return (
      <Card title="高级属性配置" bordered={false}>
        <Row gutter={16}>
          <Col span={12}>
            <FormItem
              label='阻塞策略'
              key="blockStrategy"
              name="blockStrategy"
              rules={[
                {
                  required: true,
                  message: '请选择阻塞策略',
                },
              ]}>
              <Select
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
              >
                <Option key={"DISCARD_LATER"} value={"DISCARD_LATER"} selected>{"忽略新任务"}</Option>
                <Option key={"SERIAL_EXECUTION"} value={"SERIAL_EXECUTION"}>{"串行执行"}</Option>
                <Option key={"CONCURRENT_EXECUTION"} value={"CONCURRENT_EXECUTION"}>{"并发执行"}</Option>
              </Select>
            </FormItem>
          </Col>
          <Col span={12}>
            <FormItem
              label='过期处理策略'
              key="misfireStrategy"
              name="misfireStrategy"
              rules={[
                {
                  required: true,
                  message: '请选择过期处理策略',
                },
              ]}>
              <Select
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
              >
                <Option key={"DO_NOTHING"} value={"DO_NOTHING"} selected>{"忽略"}</Option>
                <Option key={"FIRE_ONCE_NOW"} value={"FIRE_ONCE_NOW"}>{"立即执行一次"}</Option>
              </Select>
            </FormItem>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={12}>
            <FormItem
              label='最大重试次数'
              key="maxRetryTimes"
              name="maxRetryTimes"
              rules={[
                {
                  required: true,
                  message: '请提交最大重试次数',
                },
              ]}>
              <InputNumber
                style={{ width: '100%' }}
                // disabled = {formVals.position !== ''}
              >
              </InputNumber>
            </FormItem>
          </Col>
          <Col span={12}>
          </Col>
        </Row>
      </Card>
    )
  }

  return (
    <Modal
      destroyOnClose
      title="新建任务"
      visible={modalVisible}
      onCancel={() => onCancel()}
      footer={null}
      width={800}
    >
      <Form
        {...formLayout}
        style={{
          marginTop: 8,
        }}
        form={form}
        name="新建"
        initialValues={{
          public: '1',
          scheduleType: "CRON",
          blockStrategy: "DISCARD_LATER",
          misfireStrategy: "FIRE_ONCE_NOW",
          status: "1",
        }}
        onFinish={ (fields) => {
          onSubmit({
            ...fields,
            // startTime: fields.startTime.format("YYYY-MM-DD HH:mm:ss"),
            // endTime: fields.endTime.format("YYYY-MM-DD HH:mm:ss")
          });
        }}
        // onFinishFailed={onFinishFailed}
        // onValuesChange={onValuesChange}
      >
        {renderBasicField()}
        {renderSchelduleField()}
        {renderAdvanceField()}
        <Row>
          <Col span={12}></Col>
          <Col span={12}>
            <FormItem
              {...submitFormLayout}
              key="btn"
              style={{
                marginTop: 32,
              }}
            >
              <Button
                style={{
                  marginLeft: 8,
                }}
                onClick={event => {
                  event.preventDefault();
                  onCancel();
                }}
              >
                取消
              </Button>
              <Button
                style={{
                  marginLeft: 8,
                }}
                type="primary"
                htmlType="submit"
                // loading={submitting}
              >
                提交
              </Button>
            </FormItem>
          </Col>
        </Row>
      </Form>
    </Modal>
  );
};

export default CreateForm;
