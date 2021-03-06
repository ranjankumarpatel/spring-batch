package com.concretepage.springbatch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Bean
	public ItemReader<Student> reader() {
		final FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
		reader.setResource(new ClassPathResource("student-data.csv"));
		reader.setLineMapper(new DefaultLineMapper<Student>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "stdId", "subMarkOne", "subMarkTwo" });
					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Student>() {
					{
						setTargetType(Student.class);
					}
				});
			}
		});
		return reader;
	}

	@Bean
	public ItemWriter<Marksheet> writer(DataSource dataSource) {
		final JdbcBatchItemWriter<Marksheet> writer = new JdbcBatchItemWriter<Marksheet>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Marksheet>());
		writer.setSql("INSERT INTO marksheet (studentId,totalMark) VALUES (:stdId,:totalSubMark)");
		writer.setDataSource(dataSource);
		return writer;
	}

	@Bean
	public ItemProcessor<Student, Marksheet> processor() {
		return new StudentItemProcessor();
	}

	@Bean
	public Job createMarkSheet(JobBuilderFactory jobs, Step step) {
		return jobs.get("createMarkSheet").flow(step).end().build();
	}

	@Bean
	public Step step(StepBuilderFactory stepBuilderFactory, ItemReader<Student> reader, ItemWriter<Marksheet> writer,
			ItemProcessor<Student, Marksheet> processor) {
		return stepBuilderFactory.get("step").<Student, Marksheet>chunk(5).reader(reader).processor(processor)
				.writer(writer).build();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public DataSource getDataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/concretepage");
		dataSource.setUsername("root");
		dataSource.setPassword("13972684");
		return dataSource;
	}

}
