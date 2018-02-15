package simpledb;

import java.io.*;
import java.util.*;

import sun.misc.PerformanceLogger;

/**
 * HeapFile is an implementation of a DbFile that stores a collection of tuples in no particular order. Tuples are
 * stored on pages, each of which is a fixed size, and the file is simply a collection of those pages. HeapFile works
 * closely with HeapPage. The format of HeapPages is described in the HeapPage constructor.
 * 
 * @see simpledb.HeapPage#HeapPage
 */
public class HeapFile implements DbFile {

	/**
	 * The File associated with this HeapFile.
	 */
	protected File file;

	/**
	 * The TupleDesc associated with this HeapFile.
	 */
	protected TupleDesc td;

	/**
	 * Constructs a heap file backed by the specified file.
	 * 
	 * @param f
	 *            the file that stores the on-disk backing store for this heap file.
	 */
	public HeapFile(File f, TupleDesc td) {
		this.file = f;
		this.td = td;
	}

	/**
	 * Returns the File backing this HeapFile on disk.
	 * 
	 * @return the File backing this HeapFile on disk.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns an ID uniquely identifying this HeapFile. Implementation note: you will need to generate this tableid
	 * somewhere ensure that each HeapFile has a "unique id," and that you always return the same value for a particular
	 * HeapFile. We suggest hashing the absolute file name of the file underlying the heapfile, i.e.
	 * f.getAbsoluteFile().hashCode().
	 * 
	 * @return an ID uniquely identifying this HeapFile.
	 */
	public int getId() {
		return file.getAbsoluteFile().hashCode();
	}

	/**
	 * Returns the TupleDesc of the table stored in this DbFile.
	 * 
	 * @return TupleDesc of this DbFile.
	 */
	public TupleDesc getTupleDesc() {
		return td;
	}

	// see DbFile.java for javadocs
	public Page readPage(PageId pid) {
		// some code goes here
	
		long pl=pid.pageno()*BufferPool.PAGE_SIZE;
		byte[] d=new byte[BufferPool.PAGE_SIZE];
	     
		try {
			HeapPageId hid=(HeapPageId)pid;
			RandomAccessFile r=new RandomAccessFile(file,"rw");
			r.seek(pl);
			r.readFully(d);
			HeapPage hp=new HeapPage(hid, d);
			r.close();
			return hp;
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		throw new UnsupportedOperationException("Implement this");
	}

	// see DbFile.java for javadocs
	public void writePage(Page page) throws IOException {
		// some code goes here
		// not necessary for assignment1
		throw new UnsupportedOperationException("Implement this");
	}

	/**
	 * Returns the number of pages in this HeapFile.
	 */
	public int numPages() {
		return (int) (file.length() / BufferPool.PAGE_SIZE);
	}

	// see DbFile.java for javadocs
	public ArrayList<Page> addTuple(TransactionId tid, Tuple t)
			throws DbException, IOException, TransactionAbortedException {
		// some code goes here
		// not necessary for assignment1
		throw new UnsupportedOperationException("Implement this");
	}

	// see DbFile.java for javadocs
	public Page deleteTuple(TransactionId tid, Tuple t) throws DbException, TransactionAbortedException {
		// some code goes here
		// not necessary for assignment1
		throw new UnsupportedOperationException("Implement this");
	}

	// see DbFile.java for javadocs
	public DbFileIterator iterator(TransactionId tid) {

		return new DbFileIterator() {

			/**
			 * The ID of the page to read next.
			 */
			int nextPageID = 0;

			/**
			 * An iterator over all tuples from the page read most recently.
			 */
			Iterator<Tuple> iterator = null;

			@Override
			public void open() throws DbException, TransactionAbortedException {
				nextPageID = 0;
				// obtains an iterator over all tuples from page 0
				try {
					iterator = ((HeapPage) Database.getBufferPool().getPage(tid, new HeapPageId(getId(), nextPageID++),
							Permissions.READ_WRITE)).iterator();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public boolean hasNext() throws DbException, TransactionAbortedException,UnsupportedOperationException {
				// some code goes here
				//BufferPool bp;
			     int tableid=getId();
			     int pagenum=nextPageID++;
				PageId pi= new HeapPageId(tableid,pagenum);
				Iterator<Tuple> itr = null;
				try {
					itr = ((HeapPage)Database.getBufferPool().getPage(tid, pi,Permissions.READ_WRITE)).iterator();
				} 
				catch(IOException e)
				{
					e.printStackTrace();
				}
				catch(NoSuchElementException e)
				{
					e.printStackTrace();
				}
				if(itr.hasNext())
				{
					return true;
				}
				else
				{
					return false;
				}
				
				//throw new UnsupportedOperationException("Implement this");
			}
			@Override
			public Tuple next() throws DbException, TransactionAbortedException,UnsupportedOperationException {
				// some code goes here
				//Tuple tt=new Tuple(getTupleDesc());
				
				//try
				//{
				//tt=iterator.next();
				if(iterator==null)
				{
					throw new NoSuchElementException();
				}
				else
				//}
				//catch(NoSuchElementException e)
				//{
					//e.printStackTrace();
				//}
				{
					Tuple tt=iterator.next();
				return tt;
				}
				//throw new UnsupportedOperationException("Implement this");
			}

			@Override
			public void rewind() throws DbException, TransactionAbortedException {
				close();
				open();
			
			}

			@Override
			public void close() {
				iterator = null;
			}

		};
	}

}
