package com.seu.edu.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seu.edu.common.pojo.EasyUIDataGridResult;
import com.seu.edu.common.utils.EResult;
import com.seu.edu.common.utils.IDUtils;
import com.seu.edu.mapper.TbItemDescMapper;
import com.seu.edu.mapper.TbItemMapper;
import com.seu.edu.pojo.TbItem;
import com.seu.edu.pojo.TbItemDesc;
import com.seu.edu.pojo.TbItemExample;
import com.seu.edu.pojo.TbItemExample.Criteria;
import com.seu.edu.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	
	@Override
	public TbItem getItemById(long itemId) {
		
		//根据主键查询
		//TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//return tbItem;
		
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andIdEqualTo(itemId);
		//执行查询
		List<TbItem> list = itemMapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			return list.get(0);
		}
		
		return null;
	}
	
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回值对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//取分页结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		//取总记录数
		long total = pageInfo.getTotal();
		result.setTotal(total);
		return result;
	}

	@Override
	public EResult addItem(TbItem item, String desc) {
		//生成商品id
		final Long itemId = IDUtils .genItemId();
		//补全item属性
		item.setId(itemId);
		item.setStatus((byte)1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//商品标插入数据
		itemMapper.insert(item);
		//商品描述表插入数据
		TbItemDesc itemDesc = new TbItemDesc();
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		itemDescMapper.insert(itemDesc);
		//发送添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				session.createTextMessage(itemId+"");
				return null;
			}
		});
		return EResult.ok();
	}
}
