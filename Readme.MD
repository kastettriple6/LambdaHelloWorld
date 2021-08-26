This is simple Handler for AWS Lambda that producess ring loop from records in Airtable.
First of all, I found library for work with table. There are require params, like API key, app Id and table Id, which listed in config.properties.
According to task, my function must return a record with different title and two records after this, in every new second of running.
For resolve this task I use Scheduled Executor Service which create new ThreadPool of single thread and increment AtomicInteger variable every 1 second of existing this thread.
Reminder operator in method "getRecord" grants that if index of record passes last index of table - loop is back to start.
