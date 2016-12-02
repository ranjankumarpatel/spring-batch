package com.concretepage.springbatch;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@ComponentScan(basePackages = { "com.concretepage.springbatch" })
@EnableAutoConfiguration
public class Main {
	public static void main(String[] args) {
		final ApplicationContext ctx = SpringApplication.run(Main.class, args);
		final List<Marksheet> result = ctx.getBean(JdbcTemplate.class).query(
				"select studentId,totalMark FROM marksheet",
				(RowMapper<Marksheet>) (rs, row) -> new Marksheet(rs.getString(1), Integer.parseInt(rs.getString(2))));
		System.out.println("Number of Record:" + result.size());
	}
}
