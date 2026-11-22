package com.example.userlab;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.springframework.stereotype.Service;
@Service
public class RagService {
public record Chunk(String id, String text, double score) {}
private static final int DIM=512;
private double[] vector(String text) {
String s=text.toLowerCase(Locale.ROOT).replaceAll("\s+", "");
int[] cp=s.codePoints().toArray(); double[] v=new double[DIM];
for (int i=0;i+1<cp.length;i++) {
int h=31*cp[i]+cp[i+1]; v[Math.floorMod(h,DIM)]++;
}
return v;
}
private double cosine(double[] a,double[] b) {
double dot=0, aa=0, bb=0;
for (int i=0;i<DIM;i++) { dot+=a[i]*b[i]; aa+=a[i]*a[i]; bb+=b[i]*b[i]; }
return aa==0 || bb==0 ? 0 : dot/Math.sqrt(aa*bb);
}
public List<Chunk> search(String question) throws IOException {
String text=Files.readString(Path.of("knowledge/course.txt"),StandardCharsets.UTF_8);
String[] parts=text.split("(?m)^---\s*$");
if (parts.length>50 || text.length()>20000) throw new IOException("\u8d44\u6599\u8fc7\u5927");
double[] q=vector(question); List<Chunk> result=new ArrayList<>();
for (int i=0;i<parts.length;i++) {
String p=parts[i].trim(); if (p.isEmpty()) continue;
double score=cosine(q,vector(p));
if (score>0) result.add(new Chunk("S"+(i+1),p,score));
}
result.sort(Comparator.comparingDouble(Chunk::score).reversed());
return result.stream().limit(2).toList();
}
}
